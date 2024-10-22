import subprocess
import threading
import sys
import psutil
from PyQt5.QtWidgets import (
    QApplication, QLabel, QMainWindow, QVBoxLayout, QWidget, QPushButton, QGridLayout, QFrame, QHBoxLayout
)
from PyQt5.QtGui import QPixmap, QIcon, QMovie
from PyQt5.QtCore import Qt, QSize, QTimer, QDateTime, QTimeZone

from models.apps.ExploradorArchivos import ExploradorArchivos
from models.apps.calculadora import Calculadora
import os


class DesktopWindow(QMainWindow):
    def __init__(self, perfil_datos):
        super().__init__()
        print("Estos son los perfiles de los datos")
        print(perfil_datos)
        self.setWindowTitle('Escritorio Principal')
        self.setGeometry(0, 0, 1024, 768)

        self.active_apps = {}

        self.usuario_activo = perfil_datos
        self.directorio_usuario = f"./{self.usuario_activo}"  # Directorio simulado del usuario
        self.crear_carpetas_usuario()

        self.fondo = QLabel(self)
        self.loading_label = QLabel(self)


        self.mostrar_gif_carga()


        self.cargar_imagen_en_hilo('imagenes\\DesktopWallpaper.jpg')


        central_widget = QWidget(self)
        self.setCentralWidget(central_widget)
        main_layout = QVBoxLayout(central_widget)
        main_layout.setContentsMargins(0, 0, 0, 0)


        icons_layout = QGridLayout()
        icons_layout.setAlignment(Qt.AlignTop | Qt.AlignLeft)
        icons_layout.setSpacing(20)
        main_layout.addLayout(icons_layout, stretch=1)

        app_icons = [
            ('imagenes\\iconos\\aplicaciones\\papelera.png', 'Papelera'),
            ('imagenes/iconos/aplicaciones/brave-icon.png', 'Brave'),
            ('imagenes/iconos/aplicaciones/Spotify_icon.svg.png', 'Spotify'),
            ('imagenes/iconos/aplicaciones/carpeta.png', 'Archivos'),
            ('imagenes\\iconos\\aplicaciones\\rendimiento.png', 'A. Tareas'),
            ('imagenes\\iconos\\aplicaciones\\calculadora.jpg', 'Calculadora')
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

            if name == 'Calculadora':
                button.clicked.connect(self.abrir_calculadora)
            elif name == 'Spotify':
                button.clicked.connect(self.abrir_spotify)
            elif name == 'Brave':
                button.clicked.connect(self.abrir_brave)
            elif name == 'Archivos':
                button.clicked.connect(self.abrir_explorador_archivos)

            icons_layout.addWidget(icon_frame, row, col)
            col += 1
            if col == 2:
                col = 0
                row += 1


        top_layout = QHBoxLayout()
        top_layout.setContentsMargins(10, 10, 10, 10)


        self.create_top_right_controls(top_layout)
        main_layout.addLayout(top_layout)


        self.create_taskbar()

        self.showFullScreen()

    def mostrar_gif_carga(self):
        # Crear un QMovie para el GIF de carga
        self.loading_gif = QMovie("gifts/carga.gif")
        self.loading_label.setMovie(self.loading_gif)
        self.loading_label.setGeometry(self.rect())
        self.loading_label.setScaledContents(True)
        self.loading_gif.start()

    def cargar_imagen_en_hilo(self, ruta_imagen):

        hilo = threading.Thread(target=self.cargar_imagen_fondo, args=(ruta_imagen,))
        hilo.start()

    def cargar_imagen_fondo(self, ruta_imagen):

        pixmap = QPixmap(ruta_imagen)

        if pixmap.isNull():
            print("Error: No se pudo cargar la imagen.")
        else:

            self.mostrar_imagen_fondo(pixmap)

    def mostrar_imagen_fondo(self, pixmap):

        self.loading_gif.stop()
        self.loading_label.hide()

        self.fondo.setPixmap(pixmap)
        self.fondo.setScaledContents(True)
        self.fondo.setGeometry(self.rect())

    def resizeEvent(self, event):
        self.fondo.setGeometry(self.rect())
        self.loading_label.setGeometry(self.rect())
        super().resizeEvent(event)

    def create_taskbar(self):

        self.taskbar_frame = QFrame(self)
        self.taskbar_frame.setStyleSheet("background-color: rgba(38, 35, 34, 80);")


        self.taskbar_frame.setFixedHeight(110)
        self.taskbar_frame.setFixedWidth(2600)


        self.taskbar_frame.move(0, 970)


        self.taskbar_layout = QHBoxLayout(self.taskbar_frame)
        self.taskbar_layout.setContentsMargins(10, 10, 10, 10)
        self.taskbar_layout.setAlignment(Qt.AlignLeft)

    def create_top_right_controls(self, top_layout):
        # Crear frame para reloj, batería y botón de apagado
        top_right_frame = QFrame()
        top_right_frame.setFixedHeight(100)
        top_right_layout = QHBoxLayout(top_right_frame)
        top_right_layout.setContentsMargins(0, 0, 10, 0)

        # Agregar reloj
        self.clock_label = QLabel()
        self.clock_label.setStyleSheet("color: white; font-size: 18px;")
        top_right_layout.addWidget(self.clock_label)
        self.clock_label.move(500, 900)

        # Agregar porcentaje de batería
        self.battery_label = QLabel()
        self.battery_label.setStyleSheet("color: white; font-size: 14px;")
        top_right_layout.addWidget(self.battery_label, alignment=Qt.AlignRight)  # Alinear la batería a la derecha

        # Agregar botón de apagado
        shutdown_button = QPushButton()
        shutdown_button.setIcon(QIcon('imagenes\\iconos\\aplicaciones\\iconoApagado.png'))
        shutdown_button.setIconSize(QSize(50, 50))
        shutdown_button.clicked.connect(self.shutdown_system)
        shutdown_button.setStyleSheet("border: none;")
        top_right_layout.addWidget(shutdown_button, alignment=Qt.AlignRight)
        top_layout.addWidget(top_right_frame, alignment=Qt.AlignRight)

        # Temporizador para actualizar reloj y batería
        self.timer = QTimer(self)
        self.timer.timeout.connect(self.update_top_right_info)
        self.timer.start(1000)
        self.update_top_right_info()

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

    def abrir_calculadora(self):
        # Mostrar calculadora
        if 'Calculadora' not in self.active_apps:
            print("llegue hasta aqui")
            self.calculadora = Calculadora()
            print("si la abri")
            self.calculadora.setWindowFlags(Qt.WindowStaysOnTopHint | Qt.WindowTitleHint | Qt.WindowCloseButtonHint)
            self.calculadora.setWindowIcon(QIcon('imagenes\\iconos\\aplicaciones\\calculadora.jpg'))
            self.calculadora.show()
            self.add_to_taskbar('Calculadora', 'imagenes\\iconos\\aplicaciones\\calculadora.jpg', self.calculadora)
        else:
            self.calculadora.showNormal()
            self.calculadora.activateWindow()

    def abrir_spotify(self):
        # Mostrar reproductor de música
        try:
            subprocess.Popen(["C:/Users/rafam/AppData/Roaming/Spotify/Spotify.exe"])  # Ajusta el comando según la ruta de tu navegador
        except Exception as e:
            print(f"Error al abrir Spotify: {e}")

    def abrir_brave(self):
        # Comando para abrir Brave
        try:
            subprocess.Popen(["C:/Program Files/BraveSoftware/Brave-Browser/Application/brave.exe"])  # Ajusta el comando según la ruta de tu navegador
        except Exception as e:
            print(f"Error al abrir Brave: {e}")

    def add_to_taskbar(self, app_name, icon_path, app_window):
        if app_name not in self.active_apps:
            button = QPushButton()
            button.setIcon(QIcon(icon_path))
            button.setIconSize(QSize(40, 40))
            button.setStyleSheet("border: none;")
            button.clicked.connect(app_window.showNormal)  # Mostrar la aplicación al hacer clic
            button.clicked.connect(app_window.activateWindow)  # Activar la ventana
            self.taskbar_layout.addWidget(button)
            self.active_apps[app_name] = app_window  # Añadir la aplicación a las activas

    def update_top_right_info(self):
        # Actualizar reloj
        col_time_zone = QTimeZone(-18000)
        current_time = QDateTime.currentDateTime().toTimeZone(col_time_zone)
        self.clock_label.setText(current_time.toString('HH:mm:ss'))

        # Actualizar porcentaje de batería
        battery = psutil.sensors_battery()
        if battery:
            self.battery_label.setText(f" {battery.percent}%")

    def shutdown_system(self):
        # Apagar el sistema
        self.close()

    def resizeEvent(self, event):
        self.fondo.setGeometry(self.rect())
        super().resizeEvent(event)

    def keyPressEvent(self, event):
        if event.key() == Qt.Key_Escape or event.key() == Qt.Key_F11:
            if self.isFullScreen():
                self.showNormal()
            else:
                self.showFullScreen()


def main():
    app = QApplication(sys.argv)
    desktop = DesktopWindow()
    desktop.show()
    sys.exit(app.exec_())


if __name__ == '__main__':
    main()