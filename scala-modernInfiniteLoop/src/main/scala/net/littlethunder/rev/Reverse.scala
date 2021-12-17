package net.littlethunder.rev

import scala.annotation.tailrec
import scala.collection.immutable._
import scala.reflect.ClassTag

object Reverse {

  //so obvious....
  def reverse0[A](s:List[A]):List[A] = {
    s.reverse
  }

  //inefficient - appends are bad....
  def reverse1[A](s:List[A]):List[A] = {
    s match {
      case h::t => reverse1(t) :+ h
      case Nil => Nil
    }
  }

  @tailrec
  def reverse2[A](s:List[A], acc:List[A] = Nil):List[A] = {
    s match {
      case h::t => reverse2(t,h::acc)
      case h::Nil => h::acc
      case Nil => acc
    }
  }

  //This is essentially foldLeft
  def reverse3[A](s:List[A]):List[A] = {
    var ret:List[A] = Nil
    s.foreach(i => ret = i::ret)
    ret
  }

  //Array swapping
  def reverse4[A:ClassTag](s:List[A]):List[A] = {
    val ret = s.toArray
    for ((a,i) <- s.zipWithIndex.take(s.length / 2)) {
        ret(i) = ret(ret.length-1-i)
        ret(ret.length-1-i) = a
    }
    ret.toList
  }

  //foldLeft
  def reverse5[A](s:List[A]):List[A] = {
    s.foldLeft(List[A]())((a,b) => b::a)
  }
}
