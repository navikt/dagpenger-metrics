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
        .quantile(0.5, 0.05)   // Add 50th percentile (= median) with 5% tolerated error
        .quantile(0.9, 0.01)   // Add 90th percentile with 1% tolerated error
        .quantile(0.99, 0.001) // Add 99th percentile with 0.1% tolerated error
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
