package upmc.akka.ppc

import akka.actor._
import upmc.akka.ppc.DataBaseActor.{Chord, Measure, GetMeasure, ObjetMusical}

import scala.concurrent.duration.DurationInt
import scala.util.Random

object ConductorActor {
}


class ConductorActor (provider:ActorRef, player:ActorRef) extends Actor {
  import context.dispatcher
  def receive = {
    case "StartGame" => {
      val diceRoll = Random.nextInt(11) + 2
      //println("Conductor : " + diceRoll)
      provider ! GetMeasure(diceRoll)
    }
    case Measure (l) => {
      //println("Conductor : " + l)
      player ! Measure (l)
      context.system.scheduler.scheduleOnce(1800.milliseconds, self, "StartGame")

    }
    case _ => {
      println("Conductor : Other message received")
    }
  }

}