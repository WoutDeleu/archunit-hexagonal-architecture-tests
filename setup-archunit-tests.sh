#!/bin/bash

# ArchUnit Hexagonal Architecture Tests Setup Script
# This script automatically installs and configures ArchUnit tests for hexagonal architecture
# in any Java/Maven project.

set -e

# Configuration
REPO_URL="https://github.com/WoutDeleu/archunit-hexagonal-architecture-tests.git"
TEMP_DIR="/tmp/archunit-setup-$$"
LOCAL_MODE=false

# Version configuration
ARCHUNIT_VERSION="1.2.1"
JUNIT_VERSION="5.10.0"
SUREFIRE_VERSION="3.1.2"
COMPILER_PLUGIN_VERSION="3.11.0"

# Check if we're running from within the ArchUnit repository
if [[ -f "$(dirname "$0")/src/test/java/com/archunit/LayeredArchitectureTest.java" ]]; then
    LOCAL_MODE=true
    REPO_PATH="$(dirname "$0")"
fi
DEFAULT_PACKAGE="com.archunit"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

show_help() {
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Setup ArchUnit hexagonal architecture tests in your Maven project"
    echo ""
    echo "Options:"
    echo "  -p, --package PACKAGE    Target package for tests (default: auto-detect from existing tests)"
    echo "  -d, --directory DIR      Target directory (default: current directory)"
    echo "  -u, --update            Update existing tests"
    echo "  -h, --help              Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0                                     # Auto-detect package and install in archunit subfolder"
    echo "  $0 -p com.example.archunit            # Install with custom package"
    echo "  $0 -p com.mycompany.archunit -u      # Update tests with custom package"
    echo ""
}

# Parse command line arguments
TARGET_PACKAGE=""
TARGET_DIR="$(pwd)"
UPDATE_MODE=false
AUTO_DETECT=true

while [[ $# -gt 0 ]]; do
    case $1 in
        -p|--package)
            TARGET_PACKAGE="$2"
            AUTO_DETECT=false
            shift 2
            ;;
        -d|--directory)
            TARGET_DIR="$2"
            shift 2
            ;;
        -u|--update)
            UPDATE_MODE=true
            shift
            ;;
        -h|--help)
            show_help
            exit 0
            ;;
        *)
            log_error "Unknown option: $1"
            show_help
            exit 1
            ;;
    esac
done

# Validate target directory
if [[ ! -d "$TARGET_DIR" ]]; then
    log_error "Target directory does not exist: $TARGET_DIR"
    exit 1
fi

cd "$TARGET_DIR"

# Check if it's a Maven project
if [[ ! -f "pom.xml" ]]; then
    log_error "No pom.xml found in $TARGET_DIR. This script requires a Maven project."
    exit 1
fi

# Auto-detect package structure if not specified
if [[ "$AUTO_DETECT" = true ]]; then
    log_info "Auto-detecting package structure..."

    if [[ -d "src/test/java" ]]; then
        # Find the common root package (take first 2-3 segments, not the deepest)
        DETECTED_PACKAGE=$(find src/test/java -name "*.java" -exec dirname {} \; | \
                          sed 's|src/test/java/||' | \
                          tr '/' '.' | \
                          grep -v '\.archunit' | \
                          cut -d'.' -f1-3 | \
                          sort | uniq -c | sort -nr | head -1 | \
                          awk '{print $2}')

        if [[ -n "$DETECTED_PACKAGE" ]] && [[ "$DETECTED_PACKAGE" != "." ]]; then
            # Use the detected package + archunit subfolder
            TARGET_PACKAGE="$DETECTED_PACKAGE.archunit"
            log_success "Detected test package structure: $DETECTED_PACKAGE"
            log_info "Will place ArchUnit tests in: $TARGET_PACKAGE"
        else
            log_warning "Could not detect existing test package structure"
            # Fallback to main source package detection
            if [[ -d "src/main/java" ]]; then
                MAIN_PACKAGE=$(find src/main/java -name "*.java" -exec dirname {} \; | \
                              sed 's|src/main/java/||' | \
                              tr '/' '.' | \
                              cut -d'.' -f1-2 | \
                              sort | uniq -c | sort -nr | head -1 | \
                              awk '{print $2}')
                if [[ -n "$MAIN_PACKAGE" ]] && [[ "$MAIN_PACKAGE" != "." ]]; then
                    TARGET_PACKAGE="$MAIN_PACKAGE.archunit"
                    log_info "Using main package structure: $MAIN_PACKAGE"
                    log_info "Will place ArchUnit tests in: $TARGET_PACKAGE"
                else
                    TARGET_PACKAGE="$DEFAULT_PACKAGE"
                    log_warning "Using default package: $TARGET_PACKAGE"
                fi
            else
                TARGET_PACKAGE="$DEFAULT_PACKAGE"
                log_warning "Using default package: $TARGET_PACKAGE"
            fi
        fi
    else
        TARGET_PACKAGE="$DEFAULT_PACKAGE"
        log_warning "No test directory found, using default package: $TARGET_PACKAGE"
    fi
fi

log_info "Setting up ArchUnit tests in: $TARGET_DIR"
log_info "Target package: $TARGET_PACKAGE"

# Create temporary directory
mkdir -p "$TEMP_DIR"
trap "rm -rf $TEMP_DIR" EXIT

# Get the test files (either from local repository or clone)
if [[ "$LOCAL_MODE" = true ]]; then
    log_info "Using local ArchUnit repository..."
    TEST_SOURCE_DIR="$REPO_PATH"
else
    log_info "Downloading ArchUnit tests from repository..."
    git clone --depth 1 "$REPO_URL" "$TEMP_DIR/archunit-tests" > /dev/null 2>&1
    TEST_SOURCE_DIR="$TEMP_DIR/archunit-tests"
fi

# Create test directory structure
TEST_DIR="src/test/java/$(echo $TARGET_PACKAGE | tr '.' '/')"
mkdir -p "$TEST_DIR"

log_info "Creating test directory: $TEST_DIR"

# Copy test files and update package names
log_info "Installing test files..."

SOURCE_TEST_DIR="$TEST_SOURCE_DIR/src/test/java/com/archunit"
if [[ ! -d "$SOURCE_TEST_DIR" ]]; then
    log_error "Test files not found in repository"
    exit 1
fi

for test_file in "$SOURCE_TEST_DIR"/*.java; do
    if [[ -f "$test_file" ]]; then
        filename=$(basename "$test_file")
        target_file="$TEST_DIR/$filename"

        # Check if file exists and we're not in update mode
        if [[ -f "$target_file" ]] && [[ "$UPDATE_MODE" = false ]]; then
            log_warning "File $filename already exists. Use -u to update."
            continue
        fi

        log_info "Installing $filename..."

        # Copy file and update package name
        sed "s/package com\.archunit;/package $TARGET_PACKAGE;/g" "$test_file" > "$target_file"

        log_success "Installed $filename"
    fi
done

# Update pom.xml dependencies
log_info "Checking pom.xml dependencies..."

# Add version properties if they don't exist
if ! grep -q "archunit.version" pom.xml; then
    log_info "Adding version properties to pom.xml..."

    if grep -q "<properties>" pom.xml; then
        # Add properties after existing properties opening tag
        sed -i.tmp "/<properties>/a\\
        <archunit.version>$ARCHUNIT_VERSION</archunit.version>\\
        <junit.version>$JUNIT_VERSION</junit.version>\\
        <surefire.version>$SUREFIRE_VERSION</surefire.version>\\
" pom.xml
        rm -f pom.xml.tmp
        log_success "Added version properties to existing properties section"
    else
        # Create properties section
        if grep -q "</modelVersion>" pom.xml; then
            sed -i.tmp "/<\/modelVersion>/a\\
    <properties>\\
        <archunit.version>$ARCHUNIT_VERSION</archunit.version>\\
        <junit.version>$JUNIT_VERSION</junit.version>\\
        <surefire.version>$SUREFIRE_VERSION</surefire.version>\\
    </properties>\\
" pom.xml
        else
            sed -i.tmp "/<\/groupId>/a\\
    <properties>\\
        <archunit.version>$ARCHUNIT_VERSION</archunit.version>\\
        <junit.version>$JUNIT_VERSION</junit.version>\\
        <surefire.version>$SUREFIRE_VERSION</surefire.version>\\
    </properties>\\
" pom.xml
        fi
        rm -f pom.xml.tmp
        log_success "Created properties section with version properties"
    fi
else
    log_info "Version properties already present"
fi

# Check if ArchUnit dependency exists
if ! grep -q "com.tngtech.archunit" pom.xml; then
    log_info "Adding ArchUnit dependency to pom.xml..."

    # Create backup
    cp pom.xml pom.xml.backup

    # Use a more sophisticated approach to find main dependencies section
    # Create a temporary file to work with XML structure
    TEMP_POM="$TEMP_DIR/temp_pom.xml"
    cp pom.xml "$TEMP_POM"

    # Check if main dependencies section exists (not inside <build> or <plugin>)
    if awk '
        /<build>/ { in_build=1 }
        /<\/build>/ { in_build=0 }
        /<plugin>/ { in_plugin=1 }
        /<\/plugin>/ { in_plugin=0 }
        /<dependencies>/ && !in_build && !in_plugin { found_main_deps=1; exit }
        END { exit !found_main_deps }
    ' pom.xml; then
        # Main dependencies section exists, add ArchUnit after the first main <dependencies> tag
        ARCHUNIT_VERSION="$ARCHUNIT_VERSION" awk '
            /<build>/ { in_build=1 }
            /<\/build>/ { in_build=0 }
            /<plugin>/ { in_plugin=1 }
            /<\/plugin>/ { in_plugin=0 }
            /<dependencies>/ && !in_build && !in_plugin && !added {
                print $0
                print "        <!-- ArchUnit for architecture testing -->"
                print "        <dependency>"
                print "            <groupId>com.tngtech.archunit</groupId>"
                print "            <artifactId>archunit-junit5</artifactId>"
                print "            <version>\${archunit.version}</version>"
                print "            <scope>test</scope>"
                print "        </dependency>"
                added=1
                next
            }
            { print }
        ' pom.xml > "$TEMP_POM"
        mv "$TEMP_POM" pom.xml
        log_success "Added ArchUnit dependency to main dependencies section"
    else
        # No main dependencies section exists, create one
        if grep -q "<build>" pom.xml; then
            # Insert before build section
            sed -i.tmp "/<build>/i\\
    <dependencies>\\
        <!-- ArchUnit for architecture testing -->\\
        <dependency>\\
            <groupId>com.tngtech.archunit</groupId>\\
            <artifactId>archunit-junit5</artifactId>\\
            <version>\${archunit.version}</version>\\
            <scope>test</scope>\\
        </dependency>\\
    </dependencies>\\
" pom.xml
        else
            # Insert before closing project tag
            sed -i.tmp "/<\/project>/i\\
    <dependencies>\\
        <!-- ArchUnit for architecture testing -->\\
        <dependency>\\
            <groupId>com.tngtech.archunit</groupId>\\
            <artifactId>archunit-junit5</artifactId>\\
            <version>\${archunit.version}</version>\\
            <scope>test</scope>\\
        </dependency>\\
    </dependencies>\\
" pom.xml
        fi
        rm -f pom.xml.tmp
        log_success "Created main dependencies section and added ArchUnit"
    fi
else
    log_info "ArchUnit dependency already present"
fi

# Check if JUnit 5 dependency exists
if ! grep -q "junit-jupiter" pom.xml; then
    log_info "Adding JUnit 5 dependency to pom.xml..."

    # Use the same sophisticated approach for JUnit 5
    TEMP_POM="$TEMP_DIR/temp_pom_junit.xml"
    cp pom.xml "$TEMP_POM"

    # Add JUnit 5 to the first main dependencies section (not plugin dependencies)
    JUNIT_VERSION="$JUNIT_VERSION" awk '
        /<build>/ { in_build=1 }
        /<\/build>/ { in_build=0 }
        /<plugin>/ { in_plugin=1 }
        /<\/plugin>/ { in_plugin=0 }
        /<\/dependencies>/ && !in_build && !in_plugin && !added {
            print "        <!-- JUnit 5 for testing -->"
            print "        <dependency>"
            print "            <groupId>org.junit.jupiter</groupId>"
            print "            <artifactId>junit-jupiter</artifactId>"
            print "            <version>\${junit.version}</version>"
            print "            <scope>test</scope>"
            print "        </dependency>"
            print $0
            added=1
            next
        }
        { print }
    ' pom.xml > "$TEMP_POM"
    mv "$TEMP_POM" pom.xml
    log_success "Added JUnit 5 dependency to main dependencies section"
else
    log_info "JUnit 5 dependency already present"
fi

# Update Maven Surefire plugin if needed
if ! grep -q "maven-surefire-plugin" pom.xml; then
    log_info "Adding Maven Surefire plugin for test execution..."

    if grep -q "<build>" pom.xml; then
        if grep -q "<plugins>" pom.xml; then
            # Add to existing plugins section
            sed -i.tmp "/<plugins>/a\\
            <plugin>\\
                <groupId>org.apache.maven.plugins</groupId>\\
                <artifactId>maven-surefire-plugin</artifactId>\\
                <version>\\\${surefire.version}</version>\\
            </plugin>\\
" pom.xml
        else
            # Create plugins section in build
            sed -i.tmp "/<build>/a\\
        <plugins>\\
            <plugin>\\
                <groupId>org.apache.maven.plugins</groupId>\\
                <artifactId>maven-surefire-plugin</artifactId>\\
                <version>\\\${surefire.version}</version>\\
            </plugin>\\
        </plugins>\\
" pom.xml
        fi
    else
        # Create build section
        sed -i.tmp "/<\/dependencies>/a\\
    <build>\\
        <plugins>\\
            <plugin>\\
                <groupId>org.apache.maven.plugins</groupId>\\
                <artifactId>maven-surefire-plugin</artifactId>\\
                <version>\\\${surefire.version}</version>\\
            </plugin>\\
        </plugins>\\
    </build>\\
" pom.xml
    fi
    rm -f pom.xml.tmp
    log_success "Added Maven Surefire plugin"
fi

# Detect and suggest main package structure
log_info "Analyzing project structure..."

MAIN_PACKAGE=""
if [[ -d "src/main/java" ]]; then
    # Find the most common package prefix
    MAIN_PACKAGE=$(find src/main/java -name "*.java" -exec dirname {} \; | \
                   sed 's|src/main/java/||' | \
                   tr '/' '.' | \
                   cut -d'.' -f1-3 | \
                   sort | uniq -c | sort -nr | head -1 | \
                   awk '{print $2}')

    if [[ -n "$MAIN_PACKAGE" ]]; then
        log_info "Detected main package structure: $MAIN_PACKAGE"
        log_warning "Make sure your code follows hexagonal architecture patterns:"
        log_warning "  - Core domain: $MAIN_PACKAGE.core.{domain}.{model,port,usecase,exceptions}"
        log_warning "  - Adapters: $MAIN_PACKAGE.adapters.{api,database,messaging,external}.{adapter,entity}"
        log_warning "  - Infrastructure: $MAIN_PACKAGE.infrastructure.{config,util}"
    fi
fi

# Generate README for the tests
README_FILE="$TEST_DIR/README.md"
cat > "$README_FILE" << EOF
# ArchUnit Hexagonal Architecture Tests

This directory contains comprehensive ArchUnit tests for validating hexagonal architecture patterns.

## Test Files

- **LayeredArchitectureTest.java** - Core layered architecture rules
- **CoreDomainArchitectureTest.java** - Core domain layer validation
- **AdapterStructureTest.java** - Adapter layer structure validation
- **ApiAdapterArchitectureTest.java** - API adapter specific rules
- **DatabaseAdapterArchitectureTest.java** - Database adapter specific rules
- **InfrastructureArchitectureTest.java** - Infrastructure layer rules

## Running the Tests

\`\`\`bash
# Run all architecture tests
mvn test -Dtest="$TARGET_PACKAGE.*"

# Run specific test class
mvn test -Dtest="$TARGET_PACKAGE.LayeredArchitectureTest"
\`\`\`

## Package Structure Expected

Your code should follow this structure:
- **Core**: \`{package}.core.{domain}.{model,port,usecase,exceptions}\`
- **Adapters**: \`{package}.adapters.{type}.{adapter,entity}\`
- **Infrastructure**: \`{package}.infrastructure.{config,util}\`

## Customization

You can modify the package patterns in each test file to match your specific naming conventions.

Generated by ArchUnit Tests Setup Script
Source: $REPO_URL
EOF

log_success "Generated README at $README_FILE"

# Final summary
echo ""
log_success "✅ ArchUnit tests setup completed!"
echo ""
echo "📁 Installed files:"
for test_file in "$TEST_DIR"/*.java; do
    if [[ -f "$test_file" ]]; then
        echo "   - $(basename "$test_file")"
    fi
done
echo ""
echo "🔧 Next steps:"
echo "   1. Review your code structure to ensure it follows hexagonal architecture"
echo "   2. Run tests: mvn test -Dtest=\"$TARGET_PACKAGE.*\""
echo "   3. Fix any architecture violations reported by the tests"
echo "   4. Customize test rules if needed for your specific patterns"
echo ""
if [[ -n "$MAIN_PACKAGE" ]]; then
    echo "💡 Tip: Your main package appears to be '$MAIN_PACKAGE'"
    echo "   Make sure your architecture follows the expected patterns!"
fi
echo ""
log_info "For more information, see: $REPO_URL"