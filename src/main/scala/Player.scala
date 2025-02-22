package upmc.akka.ppc

import javax.sound.midi._
import javax.sound.midi.ShortMessage._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.language.postfixOps

object PlayerActor {
  case class MidiNote(pitch: Int, vel: Int, dur: Int, at: Int)

  private val info = MidiSystem.getMidiDeviceInfo.find(_.getName == "Gervill")
  private val device = info.map(MidiSystem.getMidiDevice).getOrElse {
    println("[ERROR] Could not find Gervill synthesizer.")
    sys.exit(1)
  }

  private val rcvr = device.getReceiver

  private def note_on(pitch: Int, vel: Int, chan: Int): Unit = {
    val msg = new ShortMessage()
    msg.setMessage(NOTE_ON, chan, pitch, vel)
    rcvr.send(msg, -1)
  }

  private def note_off(pitch: Int, chan: Int): Unit = {
    val msg = new ShortMessage()
    msg.setMessage(NOTE_OFF, chan, pitch, 0)
    rcvr.send(msg, -1)
  }
}

class PlayerActor() extends Actor {

  import DataBaseActor._
  import PlayerActor._

  device.open()

  override def postStop(): Unit = {
    rcvr.close()
    device.close()
  }

  def receive: Receive = {
    case Measure(l) =>
      l.foreach { chord =>
        val date = chord.date
        val notes = chord.notes
        notes.foreach { note =>
          self ! MidiNote(note.pitch, note.vol, note.dur, date)
        }
      }

    case MidiNote(p, v, d, at) =>
      //println(s"Player : $p, $v, $d, $at")
      context.system.scheduler.scheduleOnce(at milliseconds)(note_on(p, v, 0))
      context.system.scheduler.scheduleOnce((at + d) milliseconds)(note_off(p, 0))

    case _ =>
      println("Player : Other message received")
  }
}
