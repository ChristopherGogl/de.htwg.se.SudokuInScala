package de.htwg.se.sudoku

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import de.htwg.se.sudoku.aview.{GuiInterface, TuiInterface}
import de.htwg.se.sudoku.controller.controllerComponent._
import de.htwg.se.sudoku.model.fileIoComponent._
import de.htwg.se.sudoku.model.gridComponent.GridInterface
import de.htwg.se.sudoku.model.gridComponent.gridAdvancedImpl.Grid
import net.codingwell.scalaguice.ScalaModule

class SudokuModule extends AbstractModule with ScalaModule {

  val defaultSize: Int = 9

  def configure(): Unit = {
    bindConstant().annotatedWith(Names.named("DefaultSize")).to(defaultSize)
    bind[GridInterface].to[Grid]
    bind[ControllerInterface].to[controllerBaseImpl.Controller]

    bind[TuiInterface].to[aview.baseViewImpl.Tui]
    bind[GuiInterface].to[aview.baseViewImpl.gui.SwingGui]

    bind[GridInterface].annotatedWithName("tiny").toInstance(new Grid(1))
    bind[GridInterface].annotatedWithName("small").toInstance(new Grid(4))
    bind[GridInterface].annotatedWithName("normal").toInstance(new Grid(9))

    bind[FileIOInterface].to[fileIoJsonImpl.FileIO]

  }

}

class MicroSudokuModule extends AbstractModule with ScalaModule {

  val defaultSize: Int = 9

  def configure(): Unit = {
    bindConstant().annotatedWith(Names.named("DefaultSize")).to(defaultSize)
    bind[GridInterface].to[Grid]
    bind[ControllerInterface].to[controllerBaseImpl.Controller]

    bind[TuiInterface].to[aview.baseViewImpl.Tui]
    bind[GuiInterface].to[aview.baseViewImpl.gui.SwingGui]

    bind[GridInterface].annotatedWithName("tiny").toInstance(new Grid(1))
    bind[GridInterface].annotatedWithName("small").toInstance(new Grid(4))
    bind[GridInterface].annotatedWithName("normal").toInstance(new Grid(9))

    bind[FileIOInterface].to[fileIoMicroImpl.FileIO]

  }

}