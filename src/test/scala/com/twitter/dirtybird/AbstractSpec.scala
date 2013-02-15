package com.twitter.dirtybird

import org.specs.SpecificationWithJUnit
import org.specs.mock.Mockito
import org.mockito.Matchers

abstract class AbstractSpec extends SpecificationWithJUnit with Mockito {
  import Matchers.{eq => _eq}
  def argEq[T](value: T) = _eq(value)
}
