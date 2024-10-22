from PyQt5.QtWidgets import (
    QApplication, QLabel, QLineEdit, QPushButton, QVBoxLayout, QHBoxLayout, QFrame, QWidget, QMessageBox
)
from PyQt5.QtGui import QPixmap, QIcon, QRegion, QFont, QFontDatabase
from PyQt5.QtCore import Qt, QTimer, QDateTime, QTimeZone, QSize, QRect
import sys
import os
from models.escritorio import DesktopWindow  # Importar DesktopWindow desde escritorio.py

class LoginWindow(QWidget):
    def __init__(self):
        super().__init__()
        # Cargar la fuente personalizada
        font_id = QFontDatabase.addApplicationFont("Fonts\\mod_gothic.ttf")  # Asegúrate de que la fuente esté en el mismo directorio
        if font_id == -1:
            print("Error al cargar la fuente")
        else:
            font_family = QFontDatabase.applicationFontFamilies(font_id)[0]

        # Crear QLabel para la imagen de fondo
        self.fondo = QLabel(self)
        pixmap = QPixmap('imagenes/iconos/aplicaciones/LoginWallpaper.jpg')

        if pixmap.isNull():
            print("Error: No se pudo cargar la imagen.")
        else:
            self.fondo.setPixmap(pixmap)
            self.fondo.setScaledContents(True)
            self.fondo.setGeometry(self.rect())

        self.perfiles = {
            'Rafa': {'icon': 'imagenes\\iconos\\usuarios\\rafa.jpg', 'password': '1711'},
            'Mandara' : {'icon':'imagenes\\iconos\\usuarios\\mandara.jpg', 'password': '1711' }
        }

        self.showFullScreen()
        self.selected_profile = None

        main_layout = QVBoxLayout()
        main_layout.setAlignment(Qt.AlignCenter)
        self.setLayout(main_layout)

        # Reloj grande centrado en la parte superior
        self.clock_label = QLabel()

        # Aplicar la fuente personalizada al reloj
        custom_font = QFont(font_family, 80)  # Tamaño de la fuente 70px
        self.clock_label.setFont(custom_font)

        # Estilo del reloj
        self.clock_label.setStyleSheet("padding: 10px; color: white; background-color: rgba(255, 255, 255, 0);")
        self.clock_label.setAlignment(Qt.AlignCenter)
        main_layout.addWidget(self.clock_label, alignment=Qt.AlignTop | Qt.AlignCenter)
        self.clock_label.setContentsMargins(50, 120, 0, 0)  # Márgenes (izquierda, arriba, derecha, abajo)

        top_right_frame = QFrame()

        # Crear el botón de apagado
        shutdown_button = QPushButton(top_right_frame)
        icon_path = 'imagenes/iconos/aplicaciones/iconoApagado.png'

        # Comprobar si el ícono existe
        if not os.path.exists(icon_path):
            print("El icono no se encontró en la ruta especificada.")
        else:
            # Aplicar el ícono correctamente
            icon = QIcon(icon_path)
            shutdown_button.setIcon(icon)

        # Ajustar tamaño del ícono y el botón
        shutdown_button.setIconSize(QSize(32, 32))
        shutdown_button.setFixedSize(50, 50)  # Tamaño del botón

        # Conectar el botón a la función de apagado
        shutdown_button.clicked.connect(self.shutdown_system)



        # Eliminar bordes y padding para evitar desplazamientos
        shutdown_button.setStyleSheet("""
            QPushButton {
                border: none;
                padding: 0px;
                background-color: transparent;
            }
        """)

        # Posicionar el botón en la esquina superior derecha
        shutdown_button.move(top_right_frame.width() - shutdown_button.width(), 0)

        # Asegurarse de que el botón se vea al redimensionar
        shutdown_button.setContentsMargins(0, 0, 0, 0)

        # Forzar actualización para asegurar que el ícono se muestre correctamente
        shutdown_button.update()

        central_frame = QFrame()
        central_frame.setFrameShape(QFrame.StyledPanel)
        central_frame.setStyleSheet("""
            QFrame {
                background-color: rgba(0, 0, 0, 0.5);
                border-radius: 20px;
                padding: 20px;
                box-shadow: 5px 5px 15px rgba(0, 0, 0, 0.8);
            }
        """)

        # Caja redondeada y semitransparente
        central_layout = QVBoxLayout()
        central_layout.setAlignment(Qt.AlignCenter)
        central_frame.setLayout(central_layout)
        main_layout.addWidget(central_frame)

        # Añadir el icono del perfil seleccionado
        self.profile_icon = QLabel(self)
        pixmap_icon = QPixmap('imagenes\\iconos\\usuarios\\usuario_default.png')  # Icono de perfil genérico
        self.profile_icon.setPixmap(pixmap_icon)
        self.profile_icon.setAlignment(Qt.AlignCenter)
        self.profile_icon.setFixedSize(0, 100)
        central_layout.addWidget(self.profile_icon)

        self.create_profile_buttons(central_layout)

        # Nombre de usuario (se actualizará al seleccionar)
        self.username_label = QLabel("Selecciona tu usuario")
        self.username_label.setStyleSheet("font-size: 24px; font-weight: bold; color: white;")
        self.username_label.setAlignment(Qt.AlignCenter)
        central_layout.addWidget(self.username_label)

        # Campo de contraseña
        self.pass_input = QLineEdit()
        self.pass_input.setEchoMode(QLineEdit.Password)
        self.pass_input.setPlaceholderText("Ingrese su contraseña")
        self.pass_input.setFixedSize(300, 40)
        self.pass_input.setEnabled(True)  # Cambia a True para que el campo sea editable
        self.pass_input.setStyleSheet("""
            QLineEdit {
                background-color: transparent;  /* Sin color de fondo */
                color: white;
                font-size: 18px;
                border: none;  /* Sin marco */
                padding: 5px;
            }
            
        """)
        central_layout.addWidget(self.pass_input, alignment=Qt.AlignCenter)

        # Botón de login
        self.login_button = QPushButton('Iniciar Sesión')
        self.login_button.setEnabled(False)
        self.login_button.setFixedSize(300, 40)
        self.login_button.setStyleSheet("""
            QPushButton {
                    background-color: #585858; /* Gris pálido */
                    color: white; /* Texto blanco */
                    font-size: 18px;
                    border: 2px solid #c0c0c0;
                    border-radius: 10px;
                    padding: 10px 20px;
                    cursor: pointer;
                    transition: background-color 0.3s ease, transform 0.3s ease;
            }
            
            QPushButton:hover {
                    background-color: #a9a9a9; /* Gris más oscuro en hover */
                    transform: scale(1.05); /* Escala ligeramente al pasar el mouse */
                    }
            QPushButton:pressed {
                    background-color: #808080; /* Gris más oscuro en clic */
                    transform: scale(0.98); /* Botón se reduce ligeramente al clic */
            }
        """)


        self.login_button.clicked.connect(self.check_login)
        central_layout.addWidget(self.login_button, alignment=Qt.AlignCenter)

        self.pass_input.returnPressed.connect(self.check_login)

        # Botones de "Sleep", "Restart", "Shutdown"
        self.create_system_buttons()
        # Temporizador para actualizar el reloj
        self.timer = QTimer(self)
        self.timer.timeout.connect(self.update_clock)
        self.timer.start(1000)

        self.update_clock()
        self.showFullScreen()

    def create_top_right_controls(self, top_layout):
        # Crear frame para reloj, batería y botón de apagado
        top_right_frame = QFrame()
        top_right_frame.setFixedHeight(100)
        top_right_layout = QHBoxLayout(top_right_frame)
        top_right_layout.setContentsMargins(0, 0, 10, 0)
        top_right_layout.setAlignment(Qt.AlignRight)  # Alinear el layout a la derecha

        # Agregar reloj
        self.clock_label = QLabel()
        self.clock_label.setStyleSheet("color: white; font-size: 18px;")
        top_right_layout.addWidget(self.clock_label, alignment=Qt.AlignRight)  # Alinear el reloj a la derecha

        # Agregar porcentaje de batería
        self.battery_label = QLabel()
        self.battery_label.setStyleSheet("color: white; font-size: 14px;")
        top_right_layout.addWidget(self.battery_label, alignment=Qt.AlignRight)  # Alinear la batería a la derecha

        # Agregar botón de apagado
        shutdown_button = QPushButton()
        shutdown_button.setIcon(QIcon('imagenes\\iconos\\aplicaciones\\apagado.png'))
        shutdown_button.setIconSize(QSize(32, 32))
        shutdown_button.clicked.connect(self.shutdown_system)
        shutdown_button.setStyleSheet("border: none;")
        top_right_layout.addWidget(shutdown_button, alignment=Qt.AlignRight)  # Alinear el botón de apagado a la derecha

        # Añadir los controles del reloj y batería al layout superior
        top_layout.addWidget(top_right_frame, alignment=Qt.AlignRight)

        # Temporizador para actualizar reloj y batería
        self.timer = QTimer(self)
        self.timer.timeout.connect(self.update_top_right_info)
        self.timer.start(1000)
        self.update_top_right_info()

    def create_profile_buttons(self, parent_layout):
        profiles_layout = QHBoxLayout()
        profiles_layout.setAlignment(Qt.AlignCenter)
        parent_layout.addLayout(profiles_layout)

        self.profile_buttons = {}

        for perfil, datos in self.perfiles.items():
            profile_widget = QVBoxLayout()

            button = QPushButton()
            pixmap = QPixmap(datos['icon']).scaled(100, 100, Qt.KeepAspectRatioByExpanding, Qt.SmoothTransformation)

            # Crear una máscara circular
            mask = QRegion(QRect(0, 0, 100, 100), QRegion.Ellipse)
            button.setMask(mask)

            # Aplicar la imagen
            button.setIcon(QIcon(pixmap))
            button.setIconSize(QSize(100, 100))
            button.setFixedSize(100, 100)
            button.setCheckable(True)
            button.clicked.connect(lambda checked, p=perfil: self.select_profile(p))

            label = QLabel(perfil)
            label.setAlignment(Qt.AlignCenter)
            label.setStyleSheet("color: white;")

            profile_widget.addWidget(button)
            profile_widget.addWidget(label)
            profiles_layout.addLayout(profile_widget)

            self.profile_buttons[perfil] = button

    def set_background_image(self):
        # Asegurarse de que el fondo no cubra los botones
        self.fondo = QLabel(self)
        self.fondo.setPixmap(QPixmap('imagenes/iconos/fondo.jpg'))
        self.fondo.setGeometry(0, 0, self.width(), self.height())
        self.fondo.lower()  # Mover el fondo detrás de todos los widgets

    def create_system_buttons(self):
        button_style = """
            QPushButton {
                color: white;
                font-size: 18px;
                border-radius: 10px;
            }
            QPushButton:pressed {
                background-color: #454545;
            }
        """

        # Botón de Sleep
        self.sleep_button = QPushButton(self)
        self.sleep_button.setIcon(QIcon('imagenes/iconos/aplicaciones/iconSleep.png'))
        self.sleep_button.setIconSize(QSize(60, 60))
        self.sleep_button.setStyleSheet(button_style)
        self.sleep_button.clicked.connect(self.sleep_system)
        self.sleep_button.setFixedSize(100, 40)
        self.sleep_button.move(1800, 880)  # Ajustar la posición
        self.sleep_button.raise_()  # Asegurar que el botón esté al frente
        self.sleep_button.show()  # Mostrar el botón

        # Botón de Restart
        self.restart_button = QPushButton(self)
        self.restart_button.setIcon(QIcon('imagenes/iconos/aplicaciones/iconRestart.png'))
        self.restart_button.setIconSize(QSize(50, 50))
        self.restart_button.setStyleSheet(button_style)
        self.restart_button.clicked.connect(self.restart_system)
        self.restart_button.setFixedSize(100, 40)
        self.restart_button.move(1800, 940)  # Ajustar la posición
        self.restart_button.raise_()
        self.restart_button.show()  # Mostrar el botón

        # Botón de Shutdown
        self.shutdown_button = QPushButton(self)
        self.shutdown_button.setIcon(QIcon('imagenes/iconos/aplicaciones/iconoApagado.png'))
        self.shutdown_button.setIconSize(QSize(60, 60))
        self.shutdown_button.setStyleSheet(button_style)
        self.shutdown_button.clicked.connect(self.shutdown_system)
        self.shutdown_button.setFixedSize(100, 40)
        self.shutdown_button.move(1800, 1000)  # Ajustar la posición
        self.shutdown_button.raise_()
        self.shutdown_button.show()  # Mostrar el botón

    def select_profile(self, perfil):
        self.selected_profile = perfil

        for p, btn in self.profile_buttons.items():
            if p == perfil:
                btn.setStyleSheet("border: 2px solid blue;")
                btn.setChecked(True)
            else:
                btn.setStyleSheet("")
                btn.setChecked(False)

        perfil_datos = self.perfiles[perfil]
        self.username_label.setText(perfil)  # Actualizar el nombre de usuario
        pixmap_icon = QPixmap(perfil_datos['icon']).scaled(100, 100, Qt.KeepAspectRatioByExpanding,
                                                           Qt.SmoothTransformation)
        self.profile_icon.setPixmap(pixmap_icon)

        self.pass_input.setEnabled(True)
        self.login_button.setEnabled(True)
        self.pass_input.setFocus()

    def check_login(self):
        if not self.selected_profile:
            QMessageBox.warning(self, 'Error', 'Por favor, selecciona un perfil.')
            return

        contrasena = self.pass_input.text().strip()

        if not contrasena:
            QMessageBox.warning(self, 'Error', 'Por favor, completa el campo de contraseña.')
            return

        perfil_datos = self.perfiles[self.selected_profile]
        if perfil_datos['password'] == contrasena:
            self.open_desktop(self.selected_profile )
        else:
            QMessageBox.warning(self, 'Login Fallido', 'Contraseña incorrecta.')

    def open_desktop(self, perfil_datos):
        self.close()
        self.desktop_window = DesktopWindow(perfil_datos)
        self.desktop_window.showFullScreen()

    def update_clock(self):
        col_time_zone = QTimeZone(b"America/Bogota")
        current_time = QDateTime.currentDateTime()  # Obtener la hora actual sin zona horaria
        current_time = current_time.toTimeZone(col_time_zone)  # Convertirla a la zona horaria deseada
        self.clock_label.setText(current_time.toString("hh:mm:ss"))

    def sleep_system(self):
        if sys.platform == 'win32':
            os.system("rundll32.exe powrprof.dll,SetSuspendState 0,1,0")
        elif sys.platform == 'linux':
            os.system("systemctl suspend")
        elif sys.platform == 'darwin':
            os.system("osascript -e 'tell application \"System Events\" to sleep'")

    def restart_system(self):
        if sys.platform == 'win32':
            os.system("shutdown /r /t 0")
        elif sys.platform == 'linux':
            os.system("reboot")
        elif sys.platform == 'darwin':
            os.system("osascript -e 'tell app \"System Events\" to restart'")

    def shutdown_system(self):
        self.close()

    def resizeEvent(self, event):
        # Redimensionar la imagen de fondo cuando cambie el tamaño de la ventana
        self.fondo.setGeometry(self.rect())
        self.fondo.lower()  # Asegurar que el fondo siempre esté detrás

if __name__ == '__main__':
    app = QApplication(sys.argv)
    login = LoginWindow()
    login.show()
    sys.exit(app.exec_())
