package net.littlethunder.infinite

import scala.collection.Iterator.{continually, range}
import scala.util.Random

object Infinte extends App {

  for ( i <- continually(getInt())) {
    Thread.sleep(1000L)
    println(s"Int:$i")
  }

  def getInt(): Int = {
    return Random.nextInt();
  }

}
