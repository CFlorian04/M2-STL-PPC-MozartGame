package upmc.akka.ppc

import akka.actor.{Props,  Actor,  ActorRef,  ActorSystem}

object Main extends App {
  println("starting Mozart's game")

  val system = ActorSystem("MozartGame")

  val database = system.actorOf(Props[DataBaseActor], "database")
  val provider = system.actorOf(Props(new ProviderActor(database)), "provider")
  val player = system.actorOf(Props[PlayerActor], "player")
  val conductor = system.actorOf(Props(new ConductorActor(provider, player)), "conductor")

  conductor ! "StartGame"

 }
