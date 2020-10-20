# Usage

## Setup

1. Set Google Apps Script URL to `scriptUrl` in `build.gradle.kts`. 

1. Install node modules and generate external APIs.

   ```shell script
   ./gradlew generateExternalsMinimal
   ```

   It generates `build/js/node_modules` and `src/main/kotlin/externals`.

1. Clone Google Apps Script project.

   ```shell script
   ./gradlew claspLogin
   ./gradlew claspClone
   ```

   It needs `build/js/node_modules/.bin/clasp`.

# Edit

1. Edit source code in `src/main/kotlin`.

1. Build `dist`.

   ```shell script
   ./gradlew compileKotlinJs
   ```

1. Push `dist` to Google Apps Script project.

   ```shell script
   ./gradlew claspPush
   ```

## Test

1. Add a script into Google Apps Script project.

   ```javascript
   function myFunction() {
     example.printHello();
     example.printActiveSheetName();
   }
   ```

1. Run `myFunction`.

1. Check log.

   Note: `printActiveSheetName` function needs Spreadsheet. 