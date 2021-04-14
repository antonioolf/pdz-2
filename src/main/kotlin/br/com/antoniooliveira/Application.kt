package br.com.antoniooliveira

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.antoniooliveira")
		.start()
}

