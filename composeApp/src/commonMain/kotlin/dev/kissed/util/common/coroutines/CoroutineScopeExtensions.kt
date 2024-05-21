package dev.kissed.util.common.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.childScope(): CoroutineScope {
    val childJob = Job(SupervisorJob(coroutineContext[Job.Key]))
    return CoroutineScope(coroutineContext + childJob)
}