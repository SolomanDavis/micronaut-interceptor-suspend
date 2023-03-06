package live.bunch.interceptor

import io.micronaut.aop.Around

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Around(proxyTarget = true, proxyTargetMode = Around.ProxyTargetConstructorMode.WARN)
annotation class FaultyInterceptorAnnotation()
