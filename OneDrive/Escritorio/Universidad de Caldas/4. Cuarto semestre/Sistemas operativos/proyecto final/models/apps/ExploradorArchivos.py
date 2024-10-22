import os
from PyQt5.QtWidgets import (
    QDialog, QVBoxLayout, QLabel, QListWidget, QPushButton, QGridLayout, QWidget
)
from PyQt5.QtGui import QIcon
from PyQt5.QtCore import QSize

class ExploradorArchivos(QDialog):
    def __init__(self, usuario, parent=None):
        super().__init__(parent)
        self.usuario = usuario
        self.setWindowTitle('Gestor de Archivos')
        self.setGeometry(300, 300, 600, 400)

        self.layout = QVBoxLayout(self)

        self.carpeta_base = f'UsersData/{self.usuario}'
        self.carpetas = ['Videos', 'Musica', 'Descargas', 'Escritorio']
        self.iconos = {
            'Videos': 'imagenes/iconos/aplicaciones/ExploradorArchivos/videos.png',
            'Musica': 'imagenes/iconos/aplicaciones/ExploradorArchivos/imagenes.png',
            'Descargas': 'imagenes/iconos/aplicaciones/ExploradorArchivos/descargas.png',
            'Escritorio': 'imagenes/iconos/aplicaciones/ExploradorArchivos/Escritorio.png'
        }

        self.iconos_layout = QGridLayout()
        self.layout.addLayout(self.iconos_layout)

        for i, carpeta in enumerate(self.carpetas):
            button = QPushButton(carpeta)
            button.setIcon(QIcon(self.iconos[carpeta]))  # Establecer icono para cada botón
            button.setIconSize(QSize(40, 40))  # Ajustar el tamaño del icono
            button.clicked.connect(lambda _, c=carpeta: self.mostrar_archivos(c))
            self.iconos_layout.addWidget(button, i // 2, i % 2)  # Distribuir en 2 columnas

        self.list_widget = QListWidget(self)
        self.layout.addWidget(self.list_widget)

        self.setLayout(self.layout)

    def mostrar_archivos(self, carpeta):
        self.list_widget.clear()
        ruta_carpeta = os.path.join(self.carpeta_base, carpeta)

        if os.path.exists(ruta_carpeta):
            archivos = os.listdir(ruta_carpeta)
            self.list_widget.addItems(archivos)
        else:
            self.list_widget.addItem(f"No se encontró la carpeta: {carpeta}")

# En tu clase DesktopWindow, al abrir el gestor de archivos:
def abrir_explorador_archivos(self):
    self.gestor_archivos = ExploradorArchivos(self.nombre_usuario, self)  # Asumiendo que tienes el nombre del usuario
    self.gestor_archivos.show()
