# Usage

## Setup

```shell script
./gradlew generateExternalsMinimal
```

It generates `src/main/kotlin/externals` and `build/js/node_modules`.

```shell script
./gradlew claspLogin
./gradlew claspClone
```

It needs `build/js/node_modules/.bin/clasp`.

# Edit

```shell script
./gradlew compileKotlinJs
```

```shell script
./gradlew claspPush
```