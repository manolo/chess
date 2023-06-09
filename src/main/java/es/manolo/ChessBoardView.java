package es.manolo;

import java.util.Optional;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dnd.DragEndEvent;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DragStartEvent;
import com.vaadin.flow.component.dnd.DropEffect;
import com.vaadin.flow.component.dnd.DropEvent;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.dnd.EffectAllowed;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
public class ChessBoardView extends Div {
  private final VerticalLayout boardLayout;
  private final TextField fenTextField;

  public ChessBoardView() {
    boardLayout = new VerticalLayout();
    boardLayout.getStyle().set("gap", "0px");

    fenTextField = new TextField("Forsyth–Edwards Notation (FEN)");
    fenTextField.addValueChangeListener(event -> updateBoard(event.getValue()));
    fenTextField.setWidth(600, Unit.PIXELS);
    add(boardLayout);

    initializeBoard();
    boardLayout.add(fenTextField);
    fenTextField.setValue(computeFEN());
  }

  private void initializeBoard() {
    boolean isDark = false;

    for (int row = 0; row < 8; row++) {
      HorizontalLayout rowLayout = new HorizontalLayout();
      rowLayout.setSpacing(false);

      for (int col = 0; col < 8; col++) {
        Component cell = createCell(isDark, row, col);
        rowLayout.add(cell);

        isDark = !isDark;
      }

      boardLayout.add(rowLayout);
      isDark = !isDark;
    }
  }

  private void updateBoard(String fen) {
    clearBoard();
    setPiecesFromFEN(fen);
  }

  private void clearBoard() {
  }

  private void setPiecesFromFEN(String fen) {
  }

  private Component createCell(boolean isDark, int row, int col) {
    Div cell = new Div();
    cell.setWidth("50px");
    cell.setHeight("50px");

    if (isDark) {
      cell.getStyle().set("background-color", "#8b4513");
    } else {
      cell.getStyle().set("background-color", "#f0d9b5");
    }

    Label pieceLabel;
    if (row == 0 || row == 7) {
      pieceLabel = createPieceLabel(getPieceCharacter(col, row), row);
    } else if (row == 1 || row == 6) {
      pieceLabel = createPieceLabel(getPieceCharacter(col, row), row);
    } else {
      pieceLabel = createPieceLabel("", 0); // Empty label for other cells
    }

    cell.add(pieceLabel);

    if (!pieceLabel.getText().isEmpty()) {
      DragSource<Label> dragSource = DragSource.create(pieceLabel);
      dragSource.setEffectAllowed(EffectAllowed.MOVE);
      dragSource.addDragStartListener(this::onDragStart);
      dragSource.addDragEndListener(this::onDragEnd);
    }

    DropTarget<Div> dropTarget = DropTarget.create(cell);
    dropTarget.setDropEffect(DropEffect.MOVE);
    dropTarget.setActive(true);
    dropTarget.addDropListener(this::onDrop);

    return cell;
  }

  private Label createPieceLabel(String pieceCharacter, int row) {
    Label label = new Label(pieceCharacter);
    label.getStyle().set("font-family", "sans-serif");
    label.getStyle().set("font-weight", "bold");
    label.getStyle().set("display", "flex");
    label.getStyle().set("align-items", "center");
    label.getStyle().set("justify-content", "center");
    label.getStyle().set("font-size", "36px");
    return label;
  }

  private String getPieceCharacter(int col, int row) {
    if (row == 6) {
      return "\u2659"; // white pawn
    }
    if (row == 1) {
      return "\u265F"; // black pawn
    }

    switch (col) {
      case 0:
      case 7:
        return row == 7 ? "\u2656" : "\u265C"; // White or black rook
      case 1:
      case 6:
        return row == 7 ? "\u2658" : "\u265E"; // White or black knight
      case 2:
      case 5:
        return row == 7 ? "\u2657" : "\u265D"; // White or black bishop
      case 3:
        return row == 7 ? "\u2655" : "\u265B"; // White or black queen
      case 4:
        return row == 7 ? "\u2654" : "\u265A"; // White or black king
      default:
        return "";
    }
  }

  private void onDragStart(DragStartEvent<Label> event) {
    event.getComponent().getElement().getStyle().set("opacity", "0.5");
    event.getComponent().getElement().getStyle().set("cursor", "grabbing");
  }

  private void onDragEnd(DragEndEvent<Label> event) {
    event.getComponent().getElement().getStyle().remove("opacity");
    event.getComponent().getElement().getStyle().remove("cursor");
  }

  private void onDrop(DropEvent<Div> event) {
    Div targetCell = event.getComponent();
    Optional<Component> dragSourceComponent = event.getDragSourceComponent();

    dragSourceComponent.ifPresent(draggedComponent -> {
      int sourceRow = getRowFromCell(draggedComponent.getParent().orElse(null));
      int sourceCol = getColumnFromCell(draggedComponent.getParent().orElse(null));
      int targetRow = getRowFromCell(targetCell);
      int targetCol = getColumnFromCell(targetCell);

      if (isValidMove(sourceRow, sourceCol, targetRow, targetCol)) {
        targetCell.removeAll();
        targetCell.add(draggedComponent);
      }
      fenTextField.setValue(computeFEN());
    });
  }

  private boolean isValidMove(int sourceRow, int sourceCol, int targetRow, int targetCol) {
    // Add your logic to validate the move based on chess rules
    // Return true if the move is valid, false otherwise
    return true;
  }

  private int getRowFromCell(Component cell) {
    Component rowLayout = cell.getParent().orElse(null);
    if (rowLayout instanceof HorizontalLayout) {
      return boardLayout.indexOf(rowLayout);
    }
    return -1;
  }

  private int getColumnFromCell(Component cell) {
    Component rowLayout = cell.getParent().orElse(null);
    if (rowLayout instanceof HorizontalLayout) {
      return ((HorizontalLayout) rowLayout).indexOf(cell);
    }
    return -1;
  }

  private String computeFEN() {
    StringBuilder fenBuilder = new StringBuilder();

    // Board representation
    for (int row = 0; row < 8; row++) {
      int emptyCount = 0;
      for (int col = 0; col < 8; col++) {
        Component cell = getCellComponent(row, col);
        String character = getCellCharacter(cell);
        if (!character.isEmpty()) {
          if (emptyCount > 0) {
            fenBuilder.append(emptyCount);
            emptyCount = 0;
          }
          fenBuilder.append(character);
        } else {
          emptyCount++;
        }
      }
      if (emptyCount > 0) {
        fenBuilder.append(emptyCount);
      }
      if (row < 7) {
        fenBuilder.append("/");
      }
    }

    // Side to move
    fenBuilder.append(" w"); // Assuming it's always white to move

    // Castling availability
    fenBuilder.append(" -"); // Assuming no castling available

    // En passant target square
    fenBuilder.append(" -"); // Assuming no en passant target square

    // Halfmove clock
    fenBuilder.append(" 0"); // Assuming starting position

    // Fullmove number
    fenBuilder.append(" 1"); // Assuming starting position

    return replaceUnicodeToFen(fenBuilder.toString());
  }

  public String replaceUnicodeToFen(String input) {
    String[] rows = input.split("/");
    StringBuilder fenBuilder = new StringBuilder();

    for (String row : rows) {
      StringBuilder fenRowBuilder = new StringBuilder();
      for (char piece : row.toCharArray()) {
        switch (piece) {
          case '♙':
            fenRowBuilder.append('P');
            break;
          case '♖':
            fenRowBuilder.append('R');
            break;
          case '♘':
            fenRowBuilder.append('N');
            break;
          case '♗':
            fenRowBuilder.append('B');
            break;
          case '♕':
            fenRowBuilder.append('Q');
            break;
          case '♔':
            fenRowBuilder.append('K');
            break;
          case '♟':
            fenRowBuilder.append('p');
            break;
          case '♜':
            fenRowBuilder.append('r');
            break;
          case '♞':
            fenRowBuilder.append('n');
            break;
          case '♝':
            fenRowBuilder.append('b');
            break;
          case '♛':
            fenRowBuilder.append('q');
            break;
          case '♚':
            fenRowBuilder.append('k');
            break;
          default:
            fenRowBuilder.append(piece);
            break;
        }
      }
      fenBuilder.append(fenRowBuilder).append("/");
    }

    return fenBuilder.toString().replaceFirst("/$", "");
  }

  private Component getCellComponent(int row, int col) {
    HorizontalLayout rowLayout = (HorizontalLayout) boardLayout.getComponentAt(row);
    return rowLayout.getComponentAt(col);
  }

  private String getCellCharacter(Component cell) {
    Optional<Component> firstChild = cell.getChildren().findFirst();
    if (firstChild.isPresent() && firstChild.get() instanceof Label) {
      Label label = (Label) firstChild.get();
      return label.getText();
    }
    return "";
  }

}
