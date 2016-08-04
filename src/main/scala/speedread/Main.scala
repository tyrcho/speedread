package speedread

import scala.swing._

object Main extends SimpleSwingApplication {
  val top = new MainFrame {
    title = "hello"
    contents = new BoxPanel(Orientation.Vertical) {
      contents += new Label { text = "world" }
    }
  }
}