package upmc.akka.ppc

import akka.actor._
import upmc.akka.ppc.DataBaseActor.{Chord, Measure, GetMeasure, ObjetMusical}

object ProviderActor {
}

class ProviderActor (database:ActorRef) extends Actor {

  def receive = {
    case GetMeasure (num) => {

      println("Provider : " + num)
      database ! GetMeasure(num)
    }
    case Measure (l) => {
        println("Provider : " + l)
        sender ! l;
    }
    case _ => {
      println("Provider : Other message received")
    }
  }
}
