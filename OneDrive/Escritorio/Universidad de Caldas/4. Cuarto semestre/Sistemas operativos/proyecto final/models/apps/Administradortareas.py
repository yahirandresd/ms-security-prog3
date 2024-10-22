import psutil
from PyQt5.QtWidgets import QVBoxLayout, QListWidget, QDialog
import os


class AdministradorTareas(QDialog):
    def __init__(self, parent=None):
        super().__init__(parent)
        self.setWindowTitle('Administrador de Tareas')
        self.setGeometry(300, 300, 400, 300)

        self.layout = QVBoxLayout(self)
        self.procesos_lista = QListWidget(self)
        self.layout.addWidget(self.procesos_lista)

        self.cargar_procesos()

    def cargar_procesos(self):
        self.procesos_lista.clear()
        # Obtener el PID del proceso de la aplicación actual
        pid_actual = os.getpid()

        for proc in psutil.process_iter(['pid', 'name', 'cpu_percent', 'memory_info']):
            try:
                # Filtrar para mostrar solo procesos del sistema actual o que pertenezcan a la aplicación
                if proc.info['pid'] == pid_actual or proc.info['name'] == "python.exe":  # Cambia "python.exe" si usas un nombre diferente
                    info = f"PID: {proc.info['pid']} - Nombre: {proc.info['name']} - CPU: {proc.info['cpu_percent']}% - Memoria: {proc.info['memory_info'].rss / (1024 * 1024):.2f} MB"
                    self.procesos_lista.addItem(info)
            except (psutil.NoSuchProcess, psutil.AccessDenied):
                continue
