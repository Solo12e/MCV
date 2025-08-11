package com.mrclassic.mcv.api

import android.content.Context
import fi.iki.elonen.NanoHTTPD
import org.json.JSONArray
import org.json.JSONObject

class LocalHttpApiServer(private val context: Context, port: Int = 9080) : NanoHTTPD(port) {
    var authToken: String? = null

    override fun serve(session: IHTTPSession): Response {
        val token = session.headers["x-auth-token"]
        if (authToken != null && authToken != token) {
            return newFixedLengthResponse(Response.Status.UNAUTHORIZED, "application/json", jsonError("unauthorized"))
        }
        return when (session.uri) {
            "/status" -> newFixedLengthResponse(jsonOk(JSONObject().put("status", "ok")))
            "/list_configs" -> newFixedLengthResponse(jsonOk(JSONArray()))
            else -> newFixedLengthResponse(Response.Status.NOT_FOUND, "application/json", jsonError("not_found"))
        }
    }

    private fun jsonOk(data: Any) =
        JSONObject().put("ok", true).put("data", data).toString()

    private fun jsonError(msg: String) =
        JSONObject().put("ok", false).put("error", msg).toString()
}