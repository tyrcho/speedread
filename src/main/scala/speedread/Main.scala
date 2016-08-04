package speedread

import scala.swing._
import java.util.Timer
import java.util.TimerTask

object Main extends SimpleSwingApplication {
  val label = new Label {
    text = "long word blah"
    minimumSize = new Dimension(300, 100)
    verticalAlignment = Alignment.Center
  }

  val top = new MainFrame {
    title = "Speed Read"
    contents = new BoxPanel(Orientation.Vertical) {
      contents += label
    }
  }

  val stream = getClass.getResourceAsStream("/text.txt")
  val text = io.Source.fromInputStream(stream).getLines
  val words = text.flatMap(_.split(" "))

  val timer = new Timer

  val task = new TimerTask {
    override def run =
      if (words.hasNext)
        update(words.next)
  }

  timer.scheduleAtFixedRate(task, 10, 10)

  def update(word: String) = {
    Swing.onEDT {
      Thread.sleep(10)
      label.text = word
    }
  }
}