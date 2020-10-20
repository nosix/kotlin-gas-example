fun printHello() {
    console.log("Hello, Kotlin/JS!")
}

fun printActiveSheetName() {
    // TODO ./gradlew generateExternalsMinimal
    console.log(SpreadsheetApp.getActive().getSheetName())
}