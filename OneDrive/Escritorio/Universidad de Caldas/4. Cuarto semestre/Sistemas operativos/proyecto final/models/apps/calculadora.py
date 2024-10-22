from PyQt5.QtWidgets import ( # type: ignore
    QMainWindow, QLineEdit, QVBoxLayout, QHBoxLayout, QWidget, QPushButton, QGridLayout, QApplication
)
from PyQt5.QtGui import QFont # type: ignore
from PyQt5.QtCore import Qt # type: ignore
import math
import sys

class Calculadora(QMainWindow):
    def __init__(self):
        super().__init__()
        print("calculadora")
        self.setWindowTitle('Calculadora Científica')
        self.setGeometry(100, 100, 400, 600)
        self.setMinimumSize(400, 600)


        widget = QWidget(self)
        self.setCentralWidget(widget)


        layout = QVBoxLayout(widget)


        self.pantalla = QLineEdit()
        self.pantalla.setAlignment(Qt.AlignRight)
        self.pantalla.setReadOnly(True)
        self.pantalla.setFixedHeight(100)
        self.pantalla.setFont(QFont('Arial', 20))
        layout.addWidget(self.pantalla)


        botones_layout = QGridLayout()


        botones = [
            ('7', 0, 0), ('8', 0, 1), ('9', 0, 2), ('/', 0, 3),
            ('4', 1, 0), ('5', 1, 1), ('6', 1, 2), ('*', 1, 3),
            ('1', 2, 0), ('2', 2, 1), ('3', 2, 2), ('-', 2, 3),
            ('0', 3, 0), ('.', 3, 1), ('=', 3, 2), ('+', 3, 3),
            ('C', 4, 0), ('(', 4, 1), (')', 4, 2), ('^', 4, 3),
            ('sin', 5, 0), ('cos', 5, 1), ('tan', 5, 2), ('log', 5, 3),
            ('sqrt', 6, 0), ('pi', 6, 1), ('e', 6, 2), ('!', 6, 3)
        ]


        for texto, fila, columna in botones:
            boton = QPushButton(texto)
            boton.setFont(QFont('Arial', 10))
            boton.setFixedSize(60, 60)
            boton.clicked.connect(lambda checked, t=texto: self.on_click(t))
            botones_layout.addWidget(boton, fila, columna)

        layout.addLayout(botones_layout)

    def on_click(self, texto):
        if texto == '=':
            try:
                resultado = str(eval(self.pantalla.text()))
                self.pantalla.setText(resultado)
            except Exception:
                self.pantalla.setText('Error')
        elif texto == 'C':
            self.pantalla.clear()
        elif texto == 'sin':
            self.pantalla.setText(self.pantalla.text() + 'math.sin(')
        elif texto == 'cos':
            self.pantalla.setText(self.pantalla.text() + 'math.cos(')
        elif texto == 'tan':
            self.pantalla.setText(self.pantalla.text() + 'math.tan(')
        elif texto == 'log':
            self.pantalla.setText(self.pantalla.text() + 'math.log(')
        elif texto == 'sqrt':
            self.pantalla.setText(self.pantalla.text() + 'math.sqrt(')
        elif texto == 'pi':
            self.pantalla.setText(self.pantalla.text() + str(math.pi))
        elif texto == 'e':
            self.pantalla.setText(self.pantalla.text() + str(math.e))
        elif texto == '!':
            try:
                num = int(self.pantalla.text())
                resultado = math.factorial(num)
                self.pantalla.setText(str(resultado))
            except ValueError:
                self.pantalla.setText('Error')
        else:
            self.pantalla.setText(self.pantalla.text() + texto)

    def keyPressEvent(self, event):
        key = event.key()

        if key == Qt.Key_0:
            self.on_click('0')
        elif key == Qt.Key_1:
            self.on_click('1')
        elif key == Qt.Key_2:
            self.on_click('2')
        elif key == Qt.Key_3:
            self.on_click('3')
        elif key == Qt.Key_4:
            self.on_click('4')
        elif key == Qt.Key_5:
            self.on_click('5')
        elif key == Qt.Key_6:
            self.on_click('6')
        elif key == Qt.Key_7:
            self.on_click('7')
        elif key == Qt.Key_8:
            self.on_click('8')
        elif key == Qt.Key_9:
            self.on_click('9')
        elif key == Qt.Key_Plus:
            self.on_click('+')
        elif key == Qt.Key_Minus:
            self.on_click('-')
        elif key == Qt.Key_Asterisk:
            self.on_click('*')
        elif key == Qt.Key_Slash:
            self.on_click('/')
        elif key == Qt.Key_Equal or key == Qt.Key_Return:
            self.on_click('=')
        elif key == Qt.Key_Backspace:
            self.pantalla.backspace()
        elif key == Qt.Key_Escape:
            self.pantalla.clear()

  # Método para maximizar la calculadora
    def maximizar(self):
        self.showMaximized()
    
    # Método para minimizar la calculadora
    def minimizar(self):
        self.showMinimized()

    # Método para restaurar la calculadora
    def restaurar(self):
        self.showNormal()


if __name__ == '__main__':
    app = QApplication(sys.argv)
    window = Calculadora()
    window.show()
    sys.exit(app.exec_())
