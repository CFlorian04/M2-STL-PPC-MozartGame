package upmc.akka.ppc

import akka.actor._
import upmc.akka.ppc.DataBaseActor.{Chord, Measure, GetMeasure, ObjetMusical}

object ProviderActor {
  // Vous pouvez ajouter des messages ou des constantes ici si nécessaire
}

class ProviderActor(database: ActorRef) extends Actor {

  var conductor: ActorRef = _

  def receive: Receive = {
    case GetMeasure(num) =>
      conductor = sender()
      //println(s"Provider : $num")
      database ! GetMeasure(num)

    case Measure(l) =>
      //println(s"Provider : $l")
      conductor ! Measure (l)

    case _ =>
      println("Provider : Other message received")
  }
}
