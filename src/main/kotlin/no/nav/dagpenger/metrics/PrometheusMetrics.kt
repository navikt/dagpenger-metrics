package no.nav.dagpenger.metrics

import io.prometheus.client.Counter
import io.prometheus.client.Gauge
import io.prometheus.client.Histogram
import io.prometheus.client.Summary

val DAGPENGER_NAMESPACE = "dagpenger"

fun aCounter(name: String, labelNames: List<String> = listOf(), help: String): Counter {
    return Counter
        .build()
        .namespace(DAGPENGER_NAMESPACE)
        .name(name)
        .help(help)
        .labelNames(*labelNames.toTypedArray())
        .register()
}

fun aGauge(name: String, labelNames: List<String> = listOf(), help: String): Gauge {
    return Gauge
        .build()
        .namespace(DAGPENGER_NAMESPACE)
        .name(name)
        .help(help)
        .labelNames(*labelNames.toTypedArray())
        .register()
}

fun aSummary(name: String, labelNames: List<String> = listOf(), help: String): Summary {
    return Summary
        .build()
        .namespace(DAGPENGER_NAMESPACE)
        .name(name)
        .help(help)
        .labelNames(*labelNames.toTypedArray())
        .register()
}

fun aHistogram(name: String, labelNames: List<String> = listOf(), help: String): Histogram {
    return Histogram
        .build()
        .namespace(DAGPENGER_NAMESPACE)
        .name(name)
        .help(help)
        .labelNames(*labelNames.toTypedArray())
        .register()
}
