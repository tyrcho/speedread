package speedread

import scala.swing._
import java.util.Timer
import java.util.TimerTask

object Main extends SimpleSwingApplication {
  val label = new Label {
    text = "long word blah"
    minimumSize = new Dimension(300, 100)
    horizontalAlignment = Alignment.Center
  }

  val top = new MainFrame {
    title = "Speed Read"
    contents = label
  }

  val stream = getClass.getResourceAsStream("/text.txt")
  val text = io.Source.fromInputStream(stream).getLines
  val words = text.flatMap(_.split(" ")).toVector
  var pos = 0

  val timer = new Timer

  val task = new TimerTask {
    override def run =
      if (words.size > pos) {
        update(words(pos))
        pos += 1
      }
  }

  timer.scheduleAtFixedRate(task, 200, 200)

  def update(word: String) = {
    Swing.onEDT {
      Thread.sleep(10)
      label.text = word
    }
  }
}