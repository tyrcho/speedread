package speedread

import scala.swing._
import java.util.Timer
import java.util.TimerTask
import scala.swing.event._

object Main extends SimpleSwingApplication {
  var pos = 0
  var speed = 200

  val label = new Label {
    text = "long word blah"
    minimumSize = new Dimension(300, 100)
    horizontalAlignment = Alignment.Center
  }

  val speedLabel = new Label {
    text = speed + ""
  }

  val top = new MainFrame {
    title = "Speed Read"
    contents = new BorderPanel {
      add(label, BorderPanel.Position.North)
      add(speedLabel, BorderPanel.Position.South)

      listenTo(keys)
      reactions += {
        case KeyPressed(_, key, _, _) ⇒ handleKey(key)
      }
      focusable = true
      requestFocus()
    }
  }

  def handleKey(key: Key.Value) = {
    key match {
      case Key.Up =>
        updateSpeed(50)
      case Key.Down =>
        updateSpeed(-50)
    }
  }

  def updateSpeed(delta: Int) = {
    speed += delta
    speedLabel.text = speed + ""
  }
  val stream = getClass.getResourceAsStream("/text.txt")
  val text = io.Source.fromInputStream(stream).getLines
  val words = text.flatMap(_.split(" ")).toVector
  val avgLength = words.map(_.length).sum / words.length

  val timer = new Timer

  def task: TimerTask = new TimerTask {
    override def run = {
      if (words.size > pos) {
        val w = words(pos)
        update(w)
        pos += 1
        val duration = w.length * 60000 / speed / avgLength
        timer.schedule(task, duration)
      }
    }
  }

  timer.schedule(task, 60000 / speed)

  def update(word: String) = {
    Swing.onEDT {
      Thread.sleep(10)
      label.text = word
    }
  }
}