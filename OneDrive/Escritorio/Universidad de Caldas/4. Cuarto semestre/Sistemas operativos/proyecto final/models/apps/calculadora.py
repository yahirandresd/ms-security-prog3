from PyQt5.QtWidgets import (
    QMainWindow, QLineEdit, QVBoxLayout, QWidget,
    QPushButton, QGridLayout, QApplication
)
from PyQt5.QtGui import QFont, QPalette, QColor
from PyQt5.QtCore import Qt
import math
import sys

class Calculadora(QMainWindow):
    def __init__(self):
        super().__init__()
        self.setWindowTitle('Calculadora Científica')
        self.setGeometry(100, 100, 400, 600)
        self.setMinimumSize(400, 600)

        # Establecer un color de fondo
        palette = QPalette()
        palette.setColor(QPalette.Window, QColor(50, 50, 50))  # Color de fondo
        self.setPalette(palette)

        widget = QWidget(self)
        self.setCentralWidget(widget)

        layout = QVBoxLayout(widget)

        # Pantalla de la calculadora
        self.pantalla = QLineEdit()
        self.pantalla.setAlignment(Qt.AlignRight)
        self.pantalla.setReadOnly(True)
        self.pantalla.setFixedHeight(100)
        self.pantalla.setFont(QFont('Arial', 24))
        self.pantalla.setStyleSheet("background-color: white; color: black;")
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
            boton.setFont(QFont('Arial', 14))
            boton.setStyleSheet("background-color: #4CAF50; color: white; border-radius: 10px;")
            boton.setFixedSize(60, 60)
            boton.clicked.connect(lambda checked, t=texto: self.on_click(t))
            botones_layout.addWidget(boton, fila, columna)

        layout.addLayout(botones_layout)

    def on_click(self, texto):
        if texto == '=':
            try:
                # Reemplaza las funciones matemáticas para poder evaluarlas
                expresion = self.pantalla.text()
                expresion = expresion.replace('sin(', 'math.sin(')
                expresion = expresion.replace('cos(', 'math.cos(')
                expresion = expresion.replace('tan(', 'math.tan(')
                expresion = expresion.replace('log(', 'math.log(')
                expresion = expresion.replace('sqrt(', 'math.sqrt(')
                # Agregar manejo para ^ como potencia
                expresion = expresion.replace('^', '**')

                # Evaluar la expresión
                resultado = str(eval(expresion))
                self.pantalla.setText(resultado)
            except Exception as e:
                print(e)  # Imprime el error en la consola para depuración
                self.pantalla.setText('Error')
        elif texto == 'C':
            self.pantalla.clear()
        elif texto in ['sin', 'cos', 'tan', 'log', 'sqrt']:
            self.pantalla.setText(self.pantalla.text() + f'{texto}(')
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
        if key in (Qt.Key_0, Qt.Key_1, Qt.Key_2, Qt.Key_3, Qt.Key_4,
                    Qt.Key_5, Qt.Key_6, Qt.Key_7, Qt.Key_8, Qt.Key_9):
            self.on_click(str(key - Qt.Key_0))  # Convierte la tecla en su respectivo número
        elif key == Qt.Key_Plus:
            self.on_click('+')
        elif key == Qt.Key_Minus:
            self.on_click('-')
        elif key == Qt.Key_Asterisk:
            self.on_click('*')
        elif key == Qt.Key_Slash:
            self.on_click('/')
        elif key in (Qt.Key_Equal, Qt.Key_Return):
            self.on_click('=')
        elif key == Qt.Key_Backspace:
            self.pantalla.backspace()
        elif key == Qt.Key_Escape:
            self.pantalla.clear()

if __name__ == '__main__':
    app = QApplication(sys.argv)
    window = Calculadora()
    window.show()
    sys.exit(app.exec_())
