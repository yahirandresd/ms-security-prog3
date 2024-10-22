import os
import sys
import pygame
from PyQt5.QtWidgets import (
    QApplication, QMainWindow, QVBoxLayout, QPushButton, QWidget, QFileDialog, QLabel, QListWidget
)
from PyQt5.QtCore import Qt

class ReproductorMusica(QMainWindow):
    def __init__(self, directorio_musica, parent=None):
        super().__init__(parent)
        self.setWindowTitle('Reproductor de Música')
        self.setGeometry(100, 100, 400, 300)

        self.directorio_musica = directorio_musica

        # Inicializar pygame mixer
        pygame.mixer.init()

        # Crear interfaz de usuario
        self.initUI()

    def initUI(self):
        layout = QVBoxLayout()

        # Lista de archivos de música
        self.lista_musica = QListWidget()
        self.cargar_musica()
        layout.addWidget(self.lista_musica)

        # Botones
        self.boton_reproducir = QPushButton("Reproducir")
        self.boton_reproducir.clicked.connect(self.reproducir_musica)
        layout.addWidget(self.boton_reproducir)

        self.boton_detener = QPushButton("Detener")
        self.boton_detener.clicked.connect(self.detener_musica)
        layout.addWidget(self.boton_detener)

        # Configuración del widget central
        widget = QWidget()
        widget.setLayout(layout)
        self.setCentralWidget(widget)

    def cargar_musica(self):
        # Cargar archivos de música de la carpeta
        for archivo in os.listdir(self.directorio_musica):
            if archivo.endswith(('.mp3', '.wav', '.ogg')):  # Extensiones de archivos de audio soportadas
                self.lista_musica.addItem(archivo)

    def reproducir_musica(self):
        # Reproducir la música seleccionada
        item = self.lista_musica.currentItem()
        if item:
            archivo_musica = os.path.join(self.directorio_musica, item.text())
            pygame.mixer.music.load(archivo_musica)
            pygame.mixer.music.play()

    def detener_musica(self):
        # Detener la música
        pygame.mixer.music.stop()


