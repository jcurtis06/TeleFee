package io.jcurtis.telefee.request

import java.util.*

object TPRequestManager {
    private val requests = mutableListOf<TPRequest>()

    fun addRequest(request: TPRequest) {
        requests.add(request)
    }

    fun removeRequest(request: TPRequest) {
        requests.remove(request)
    }

    fun getRecentRequest(target: UUID): TPRequest? {
        return requests.find { it.target == target }
    }

    fun getAllRequests(target: UUID): List<TPRequest> {
        return requests.filter { it.target == target }
    }
}
