package no.nav.dagpenger.metrics

import io.prometheus.client.Counter

fun aCounter(name: String, labelNames: List<String> = listOf(), helptext: String): io.prometheus.client.Counter {
    return Counter.build()
        .namespace("dagpenger")
        .name(name)
        .labelNames(labelNames)
        .help(helptext).register()
}