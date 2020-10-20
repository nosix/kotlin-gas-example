import java.io.FileReader
import java.io.FileWriter
import com.google.gson.JsonObject
import com.google.gson.Gson
import com.google.gson.JsonArray
import java.nio.file.Paths

fun appendFilePushOrderAttr(claspJsonPath: String) {
    val filePushOrderKey = "filePushOrder"
    val claspJson = readJson(claspJsonPath)
    if (!claspJson.has(filePushOrderKey)) {
        val rootDir = claspJson.getAsJsonPrimitive("rootDir").asString ?: ""
        val kotlinJsPath = Paths.get(rootDir, "kotlin.js").toString()
        claspJson.add(filePushOrderKey, JsonArray().apply { add(kotlinJsPath) })
    }
    writeJson(claspJsonPath, claspJson)
}

private fun readJson(claspJsonPath: String): JsonObject {
    return Gson().fromJson(FileReader(claspJsonPath), JsonObject::class.java)
}

private fun writeJson(claspJsonPath: String, jsonData: JsonObject) {
    FileWriter(claspJsonPath).use { out ->
        out.write(Gson().toJson(jsonData))
    }
}