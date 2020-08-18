package com.github.who.zio

import com.github.who.zio.CreateEffect.{ zioEither, zioOption }
import zio.test.Assertion.equalTo
import zio.test.environment.TestEnvironment
import zio.test.{ assertM, suite, testM, DefaultRunnableSpec, ZSpec }

object CreateEffectSpec extends DefaultRunnableSpec {

  override def spec: ZSpec[TestEnvironment, Any] =
    suite("CreateEffectSpec")(
      testM("zioOption") {
        assertM(zioOption("id1"))(
          equalTo(Some((EffectUser("id1", "Alice", "t1"), Team("t1", "Alice1"))))
        )
      },
      testM("zioEither") {
        val jsonStr: String =
          """
            |{
            |  "id": "1",
            |  "name": "Alice",
            |  "teamId": "t1"
            |}
            |""".stripMargin
        assertM(zioEither(jsonStr))(
          //equalTo(Some((EffectUser("id1", "Alice", "t1"), Team("t1", "Alice1"))))
          equalTo(Some((EffectUser("1", "Alice", "t1"), Team("t1", "Alice1"))))
        )
      }
    )
}
