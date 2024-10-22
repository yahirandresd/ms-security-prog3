import psutil
from PyQt5.QtWidgets import QVBoxLayout, QListWidget, QDialog, QLabel, QGroupBox
import os

class AdministradorTareas(QDialog):
    def __init__(self, parent=None):
        super().__init__(parent)
        self.setWindowTitle('Administrador de Tareas')
        self.setGeometry(300, 300, 400, 300)

        self.layout = QVBoxLayout(self)

        # Crear lista de procesos
        self.procesos_lista = QListWidget(self)
        self.layout.addWidget(self.procesos_lista)

        # Crear un grupo para estadísticas del sistema
        self.estadisticas_box = QGroupBox("Estadísticas del Sistema", self)
        self.estadisticas_layout = QVBoxLayout(self.estadisticas_box)
        self.layout.addWidget(self.estadisticas_box)

        # Etiquetas para mostrar el estado de la CPU, batería y SSD
        self.cpu_label = QLabel(self)
        self.battery_label = QLabel(self)
        self.ssd_label = QLabel(self)

        self.estadisticas_layout.addWidget(self.cpu_label)
        self.estadisticas_layout.addWidget(self.battery_label)
        self.estadisticas_layout.addWidget(self.ssd_label)

        self.cargar_procesos()
        self.cargar_estadisticas()

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

    def cargar_estadisticas(self):
        # Obtener el uso de la CPU
        cpu_usage = psutil.cpu_percent(interval=1)
        self.cpu_label.setText(f"Uso de CPU: {cpu_usage}%")

        # Obtener el estado de la batería
        battery = psutil.sensors_battery()
        if battery:
            percent = battery.percent
            plugged = battery.power_plugged
            self.battery_label.setText(f"Batería: {percent}% {'(Cargando)' if plugged else '(No cargando)'}")
        else:
            self.battery_label.setText("Batería: N/A")

        # Obtener información sobre el disco SSD
        disk_usage = psutil.disk_usage('/')
        free_space = disk_usage.free / (1024 * 1024 * 1024)  # Convertir a GB
        total_space = disk_usage.total / (1024 * 1024 * 1024)  # Convertir a GB
        self.ssd_label.setText(f"Espacio SSD: {free_space:.2f} GB libres de {total_space:.2f} GB totales")

if __name__ == '__main__':
    from PyQt5.QtWidgets import QApplication
    import sys

    app = QApplication(sys.argv)
    dialog = AdministradorTareas()
    dialog.show()
    sys.exit(app.exec_())
