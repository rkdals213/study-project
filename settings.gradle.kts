plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "study-project"


include("module-batch")
include("module-batch:test01-basic")
findProject(":module-batch:test01-basic")?.name = "test01-basic"


include("module-batch:test02-paging")
findProject(":module-batch:test02-paging")?.name = "test02-paging"

include("module-batch:test03-exposed")
findProject(":module-batch:test03-exposed")?.name = "test03-exposed"

include("module-batch:test04-kafka")
findProject(":module-batch:test04-kafka")?.name = "test04-kafka"

include("module-batch:test05-redis")
findProject(":module-batch:test05-redis")?.name = "test05-redis"

include("module-batch:test06-datasource")
findProject(":module-batch:test06-datasource")?.name = "test06-datasource"

include("module-batch:test07-dynamic-query")
findProject(":module-batch:test07-dynamic-query")?.name = "test07-dynamic-query"

include("module-web")

include("module-web:test01-api-response")
findProject(":module-web:test01-api-response")?.name = "test01-api-response"

include("module-web:test02-api-result")
findProject(":module-web:test02-api-result")?.name = "test02-api-result"

include("module-web:test03-class-validation")
findProject(":module-web:test03-class-validation")?.name = "test03-class-validation"

include("module-web:test04-kotlin-dsl")
findProject(":module-web:test04-kotlin-dsl")?.name = "test04-kotlin-dsl"

include("module-web:test05-function-extension")
findProject(":module-web:test05-function-extension")?.name = "test05-function-extension"

include("module-web:test06-null-check")
findProject(":module-web:test06-null-check")?.name = "test06-null-check"

include("module-web:test07-test-isolation")
findProject(":module-web:test07-test-isolation")?.name = "test07-test-isolation"

include("module-web:test08-virtual-thread")
findProject(":module-web:test08-virtual-thread")?.name = "test08-virtual-thread"

include("module-web:test09-exposed")
findProject(":module-web:test09-exposed")?.name = "test09-exposed"

include("module-web:test10-either")
findProject(":module-web:test10-either")?.name = "test10-either"

include("module-web:test11-kafka-consumer")
findProject(":module-web:test11-kafka-consumer")?.name = "test11-kafka-consumer"

include("module-web:test12-redis-jwt")
findProject(":module-web:test12-redis-jwt")?.name = "test12-redis-jwt"

include("module-web:test13-outbox-pattern")
findProject(":module-web:test13-outbox-pattern")?.name = "test13-outbox-pattern"

include("module-web:test14-reservation")
findProject(":module-web:test14-reservation")?.name = "test14-reservation"

include("module-web:test15-redis-list")
findProject(":module-web:test15-redis-list")?.name = "test15-redis-list"

include("module-web:test16-transactional")
findProject(":module-web:test16-transactional")?.name = "test16-transactional"

include("module-web:test17-test-fixtures")
include("module-web:test17-test-fixtures:module-domain")
findProject(":module-web:test17-test-fixtures:module-domain")?.name = "test17-module-domain"
include("module-web:test17-test-fixtures:module-service")
findProject(":module-web:test17-test-fixtures:module-service")?.name = "test17-module-service"

include("module-web:test18-kotest-di")
findProject(":module-web:test18-kotest-di")?.name = "test18-kotest-di"

include("module-web:test19-query")
findProject(":module-web:test19-query")?.name = "test19-query"

include("module-web:test20-multi-datasource")
findProject(":module-web:test20-multi-datasource")?.name = "test20-multi-datasource"

include("module-web:test21-money")
findProject(":module-web:test21-money")?.name = "test21-money"

include("module-web:test22-design-pattern")
findProject(":module-web:test22-design-pattern")?.name = "test22-design-pattern"

include("module-database")
include("module-database:jpa")
findProject(":module-database-data:jpa")?.name = "jpa"

include("module-database:exposed")
findProject(":module-database-data:exposed")?.name = "exposed"
