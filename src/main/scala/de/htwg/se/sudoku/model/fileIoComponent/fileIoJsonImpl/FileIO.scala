package de.htwg.se.sudoku.model.fileIoComponent.fileIoJsonImpl

import com.google.inject.Guice
import com.google.inject.name.Names
import de.htwg.se.sudoku.SudokuModule
import de.htwg.se.sudoku.model.fileIoComponent.FileIOInterface
import de.htwg.se.sudoku.model.gridComponent.GridInterface
import net.codingwell.scalaguice.InjectorExtensions._
import play.api.libs.json._

import scala.io.Source
import scala.util.Try

class FileIO extends FileIOInterface {

  final val FILE_NAME: String = "grid.json"

  override def load: Try[Option[GridInterface]] = {
    var gridOption: Option[GridInterface] = None

    Try {
      val source: String = Source.fromFile(FILE_NAME).getLines.mkString

      val json: JsValue = Json.parse(source)
      val size = (json \ "grid" \ "size").get.toString.toInt
      val injector:ScalaInjector = Guice.createInjector(new SudokuModule)

      size match {
        case 1 =>
          gridOption =
            Some(injector.instance[GridInterface](Names.named("tiny")))
        case 4 =>
          gridOption =
            Some(injector.instance[GridInterface](Names.named("small")))
        case 9 =>
          gridOption =
            Some(injector.instance[GridInterface](Names.named("normal")))
        case _ =>
      }
      gridOption match {
        case Some(grid) => {
          var _grid = grid
          for (index <- 0 until size * size) {
            val row = (json \\ "row") (index).as[Int]
            val col = (json \\ "col") (index).as[Int]
            val cell = (json \\ "cell") (index)
            val value = (cell \ "value").as[Int]
            _grid = _grid.set(row, col, value)
            val given = (cell \ "given").as[Boolean]
            val showCandidates = (cell \ "showCandidates").as[Boolean]
            if (given) _grid = _grid.setGiven(row, col, value)
            if (showCandidates) _grid = _grid.setShowCandidates(row, col)
          }
          gridOption = Some(_grid)
        }
        case None =>
      }
      gridOption
    }
  }

  override def save(grid: GridInterface): Try[Unit] = {
    import java.io._

    Try {
      val pw = new PrintWriter(new File(FILE_NAME))
      pw.write(Json.prettyPrint(gridToJson(grid)))
      pw.close
    }
  }

  def gridToJson(grid: GridInterface) = grid.toJson

}
