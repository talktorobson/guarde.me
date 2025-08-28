# 🤖 Android SDK Setup Guide - Fastest Method

## 🚀 **Option 1: Homebrew Setup (Recommended - 20 minutes)**

### Step 1: Install Android SDK and Gradle
```bash
# Install Android SDK command line tools
brew install --cask android-sdk

# Install Gradle
brew install gradle

# Install Android platform tools
brew install --cask android-platform-tools
```

### Step 2: Setup Environment Variables
```bash
# Add to ~/.zshrc or ~/.bash_profile
echo 'export ANDROID_HOME=/usr/local/share/android-sdk' >> ~/.zshrc
echo 'export PATH=$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$PATH' >> ~/.zshrc

# Reload shell
source ~/.zshrc
```

### Step 3: Install Required Android Components
```bash
# Accept licenses
$ANDROID_HOME/tools/bin/sdkmanager --licenses

# Install required Android API levels and build tools
$ANDROID_HOME/tools/bin/sdkmanager "platforms;android-34" "build-tools;34.0.0" "platform-tools"
```

### Step 4: Setup Gradle Wrapper in Project
```bash
cd /Users/20015403/Documents/PROJECTS/personal/guarde-me/android-app

# Initialize Gradle wrapper
gradle wrapper --gradle-version 8.4

# Make gradlew executable
chmod +x gradlew
```

### Step 5: Build the Project
```bash
# Clean and build
./gradlew clean build

# Or just check if everything compiles
./gradlew assembleDebug
```

---

## 🖥️ **Option 2: Android Studio (Full IDE - 45 minutes)**

### Step 1: Download Android Studio
```bash
# Download from: https://developer.android.com/studio
# Or via Homebrew:
brew install --cask android-studio
```

### Step 2: Run Android Studio Setup
1. Launch Android Studio
2. Follow setup wizard
3. Install SDK components (API 34, Build Tools 34.0.0)
4. Configure SDK path

### Step 3: Open Project
1. File → Open → Select android-app folder
2. Let Android Studio sync Gradle
3. Build → Make Project

---

## ⚡ **Option 3: Command Line Tools Only (Minimal - 30 minutes)**

### Step 1: Download SDK Command Line Tools
```bash
# Create SDK directory
mkdir -p ~/Android/Sdk
cd ~/Android/Sdk

# Download command line tools
curl -O https://dl.google.com/android/repository/commandlinetools-mac-9477386_latest.zip
unzip commandlinetools-mac-9477386_latest.zip
mv cmdline-tools latest
mkdir cmdline-tools
mv latest cmdline-tools/
```

### Step 2: Setup Environment
```bash
export ANDROID_HOME=~/Android/Sdk
export PATH=$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$PATH

# Add to shell profile
echo 'export ANDROID_HOME=~/Android/Sdk' >> ~/.zshrc
echo 'export PATH=$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$PATH' >> ~/.zshrc
```

### Step 3: Install Components
```bash
# Accept licenses
sdkmanager --licenses

# Install required components
sdkmanager "platforms;android-34" "build-tools;34.0.0" "platform-tools"

# Install Gradle separately
brew install gradle
```

---

## 🧪 **Verification Commands**

After setup, verify everything works:

```bash
# Check Android SDK
echo $ANDROID_HOME
ls $ANDROID_HOME

# Check Gradle
gradle --version

# Check Android tools
adb --version

# Test project build
cd /Users/20015403/Documents/PROJECTS/personal/guarde-me/android-app
./gradlew assembleDebug
```

---

## 🎯 **Expected Results After Setup**

### ✅ **Successful Build Output:**
```
BUILD SUCCESSFUL in 2m 30s
45 actionable tasks: 45 executed
```

### ✅ **Generated APK Location:**
```
android-app/app/build/outputs/apk/debug/app-debug.apk
```

### ✅ **What You Can Do:**
- Compile the Android app locally
- Generate APK for device testing
- Make code changes and rebuild
- Run static analysis and tests

---

## ⚠️ **Common Issues & Solutions**

### Issue: "SDK not found"
```bash
# Solution: Set ANDROID_HOME correctly
export ANDROID_HOME=~/Android/Sdk  # or wherever you installed it
```

### Issue: "Gradle wrapper not found"
```bash
# Solution: Regenerate wrapper
cd android-app
gradle wrapper --gradle-version 8.4
chmod +x gradlew
```

### Issue: "Build tools version not found"
```bash
# Solution: Install correct build tools version
sdkmanager "build-tools;34.0.0"
```

### Issue: Java version compatibility
```bash
# Our project uses Java 8 target, but Java 21 is fine for building
# If issues occur, you might need Java 17:
brew install openjdk@17
export JAVA_HOME=/opt/homebrew/opt/openjdk@17
```

---

## ⏱️ **Time Estimates**

- **Homebrew method**: 15-20 minutes
- **Android Studio**: 30-45 minutes  
- **Command line only**: 20-30 minutes
- **First build**: 5-10 minutes (downloads dependencies)
- **Subsequent builds**: 30-60 seconds

---

## 🚀 **Quick Start (If you want to start now)**

**Just run these 5 commands:**

```bash
# 1. Install tools
brew install --cask android-sdk gradle

# 2. Set environment  
echo 'export ANDROID_HOME=/usr/local/share/android-sdk' >> ~/.zshrc
source ~/.zshrc

# 3. Install Android components
$ANDROID_HOME/tools/bin/sdkmanager --licenses
$ANDROID_HOME/tools/bin/sdkmanager "platforms;android-34" "build-tools;34.0.0"

# 4. Setup project
cd /Users/20015403/Documents/PROJECTS/personal/guarde-me/android-app
gradle wrapper --gradle-version 8.4

# 5. Build!
./gradlew assembleDebug
```

**That's it! You'll have a compiled APK ready for testing.**