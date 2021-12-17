package net.littlethunder.rev

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should

class CubeCalculatorTest extends AnyFlatSpec with should.Matchers {
  val listNil = Nil
  val listOne = List("one")
  val listTwo = List("one","two")
  val listThree = List("one","two","three")
  val listFour = List("one","two","three","four")

  "reverse0" must "reverse things" in {
    assert(Nil == Reverse.reverse0(listNil))
    assert(listOne == Reverse.reverse0(listOne))
    assert(listTwo.reverse == Reverse.reverse0(listTwo))
    assert(listThree.reverse == Reverse.reverse0(listThree))
    assert(listFour.reverse == Reverse.reverse0(listFour))
  }

  "reverse1" must "reverse things" in {
    assert(Nil == Reverse.reverse1(listNil))
    assert(listOne == Reverse.reverse1(listOne))
    assert(listTwo.reverse == Reverse.reverse1(listTwo))
    assert(listThree.reverse == Reverse.reverse1(listThree))
    assert(listFour.reverse == Reverse.reverse1(listFour))
  }

  "reverse2" must "reverse things" in {
    assert(Nil == Reverse.reverse2(listNil))
    assert(listOne == Reverse.reverse2(listOne))
    assert(listTwo.reverse == Reverse.reverse2(listTwo))
    assert(listThree.reverse == Reverse.reverse2(listThree))
    assert(listFour.reverse == Reverse.reverse2(listFour))
  }

  "reverse3" must "reverse things" in {
    assert(Nil == Reverse.reverse3(listNil))
    assert(listOne == Reverse.reverse3(listOne))
    assert(listTwo.reverse == Reverse.reverse3(listTwo))
    assert(listThree.reverse == Reverse.reverse3(listThree))
    assert(listFour.reverse == Reverse.reverse3(listFour))
  }

  "reverse4" must "reverse things" in {
    assert(Nil == Reverse.reverse4(listNil))
    assert(listOne == Reverse.reverse4(listOne))
    assert(listTwo.reverse == Reverse.reverse4(listTwo))
    assert(listThree.reverse == Reverse.reverse4(listThree))
    assert(listFour.reverse == Reverse.reverse4(listFour))
  }

  "reverse5" must "reverse things" in {
    assert(Nil == Reverse.reverse5(listNil))
    assert(listOne == Reverse.reverse5(listOne))
    assert(listTwo.reverse == Reverse.reverse5(listTwo))
    assert(listThree.reverse == Reverse.reverse5(listThree))
    assert(listFour.reverse == Reverse.reverse5(listFour))
  }
}

