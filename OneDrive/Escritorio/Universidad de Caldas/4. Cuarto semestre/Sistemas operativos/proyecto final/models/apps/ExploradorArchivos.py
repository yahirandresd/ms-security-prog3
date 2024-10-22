# explorador_archivos.py
import sys
from PyQt5.QtWidgets import QMainWindow, QTreeView, QFileSystemModel


import os
import sys
from PyQt5.QtWidgets import (
    QApplication, QMainWindow, QVBoxLayout, QWidget, QPushButton, QGridLayout, QFrame, QLabel, QTreeView, QFileSystemModel
)
from PyQt5.QtGui import QPixmap, QIcon
from PyQt5.QtCore import Qt, QSize


class DesktopWindow(QMainWindow):
    def __init__(self, usuario_activo):
        super().__init__()

        self.usuario_activo = usuario_activo
        self.directorio_usuario = f"./{self.usuario_activo}"  # Directorio simulado del usuario

        # Crear las carpetas predefinidas
        self.crear_carpetas_usuario()

        # Configuración de la ventana principal del escritorio
        self.setWindowTitle(f'Escritorio de {self.usuario_activo}')
        self.setGeometry(0, 0, 1024, 768)
        self.active_apps = {}

        central_widget = QWidget(self)
        self.setCentralWidget(central_widget)
        main_layout = QVBoxLayout(central_widget)
        main_layout.setContentsMargins(0, 0, 0, 0)

        # Layout de iconos en el escritorio
        icons_layout = QGridLayout()
        icons_layout.setAlignment(Qt.AlignTop | Qt.AlignLeft)
        icons_layout.setSpacing(20)
        main_layout.addLayout(icons_layout, stretch=1)

        # Lista de iconos en el escritorio
        app_icons = [
            ('imagenes\\iconos\\aplicaciones\\carpeta.png', 'Archivos'),  # Icono de explorador de archivos
        ]

        row, col = 0, 0
        for icon_path, name in app_icons:
            icon_frame = QFrame()
            icon_frame.setFixedSize(100, 150)
            icon_layout = QVBoxLayout(icon_frame)
            icon_layout.setAlignment(Qt.AlignCenter)
            icon_layout.setContentsMargins(0, 0, 0, 0)

            button = QPushButton()
            pixmap = QPixmap(icon_path)
            if not pixmap.isNull():
                button.setIcon(QIcon(pixmap))
                button.setIconSize(QSize(80, 80))
            button.setFixedSize(80, 80)
            button.setStyleSheet("border: none;")

            app_label = QLabel(name)
            app_label.setAlignment(Qt.AlignCenter)
            app_label.setStyleSheet("color: white;")

            icon_layout.addWidget(button)
            icon_layout.addWidget(app_label)

            if name == 'Archivos':
                button.clicked.connect(self.abrir_explorador_archivos)

            icons_layout.addWidget(icon_frame, row, col)
            col += 1
            if col == 2:
                col = 0
                row += 1

        self.showFullScreen()

    def crear_carpetas_usuario(self):
        # Crear las carpetas para cada usuario
        carpetas = ['Videos', 'Musica', 'Descargas', 'Escritorio']
        if not os.path.exists(self.directorio_usuario):
            os.makedirs(self.directorio_usuario)  # Crear el directorio base del usuario

        for carpeta in carpetas:
            carpeta_path = os.path.join(self.directorio_usuario, carpeta)
            if not os.path.exists(carpeta_path):
                os.makedirs(carpeta_path)  # Crear cada una de las carpetas

    def abrir_explorador_archivos(self):
        # Crear una ventana separada para el explorador de archivos
        self.explorador_archivos = ExploradorArchivos(self.directorio_usuario)
        self.explorador_archivos.setWindowIcon(QIcon('imagenes\\iconos\\aplicaciones\\carpeta.png'))
        self.explorador_archivos.show()


class ExploradorArchivos(QMainWindow):
    def __init__(self, directorio_usuario):
        super().__init__()

        self.setWindowTitle('Explorador de Archivos')
        self.setGeometry(100, 100, 800, 600)

        # Crear el modelo para el sistema de archivos
        self.model = QFileSystemModel()
        self.model.setRootPath(directorio_usuario)

        # Crear la vista para el explorador de archivos
        self.tree = QTreeView()
        self.tree.setModel(self.model)
        self.tree.setRootIndex(self.model.index(directorio_usuario))
        self.tree.setColumnWidth(0, 250)  # Ajustar el ancho de las columnas
        self.setCentralWidget(self.tree)


def main():
    app = QApplication(sys.argv)
    usuario_activo = 'Rafa'  # El nombre de usuario lo obtienes del login
    desktop = DesktopWindow(usuario_activo)
    desktop.show()
    sys.exit(app.exec_())


if __name__ == '__main__':
    main()
