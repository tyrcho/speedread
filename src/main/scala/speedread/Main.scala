package speedread

import scala.swing._
import GridBagPanel._
import java.util.Timer
import java.util.TimerTask
import scala.swing.event._
import javax.swing.Box

object Main extends SimpleSwingApplication {
  var pos = 0
  var speed = 200

  val wordLeft = new Label {
    horizontalAlignment = Alignment.Right
  }
  val wordMid = new Label(" ")
  val wordRight = new Label {
    horizontalAlignment = Alignment.Left
  }

  val wordPanel = new GridBagPanel {
    val c = new Constraints
    c.fill = Fill.None
    c.gridy = 0
    c.gridx = 0
    layout(new Label("                ")) = c
    c.gridx = 1
    layout(new Label("|")) = c
    c.gridx = 2
    layout(new Label("                ")) = c

    c.gridy = 1
    c.gridx = 0
    c.anchor = Anchor.East
    layout(wordLeft) = c
    c.gridx = 1
    layout(wordMid) = c
    c.gridx = 2
    c.anchor = Anchor.West
    layout(wordRight) = c
    c.anchor = Anchor.Center

    c.gridy = 2
    c.gridx = 1
    layout(new Label("|")) = c

  }

  val speedLabel = new Label {
    text = speed + ""
  }

  val top = new MainFrame {
    title = "Speed Read"
    contents = new BorderPanel {
      add(wordPanel, BorderPanel.Position.North)
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
        val specialChar = w.exists(!_.isLetter)
        val size = if (specialChar) w.length max avgLength else w.length
        val duration = size * 60000 / speed / avgLength
        val dur = if (specialChar) 2 * duration else duration
        timer.schedule(task, dur)
      }
    }
  }

  timer.schedule(task, 60000 / speed)

  def update(word: String) = {
    Swing.onEDT {
      val (l, m, r) = split(word)
      wordLeft.text = l
      wordMid.text = m
      wordRight.text = r
    }
  }

  def split(word: String) = {
    val pos = ((word.length + 1) * 0.4).toInt - 1
    (word.take(pos), word.drop(pos).take(1), word.drop(pos + 1))
  }
}