import psutil
import mysql.connector
from datetime import datetime

# Funções para capturar percentuais de uso da CPU, memória e disco
def get_cpu_usage():
    return psutil.cpu_percent(interval=1)

def get_memory_usage():
    return psutil.virtual_memory().percent

def get_disk_usage():
    return psutil.disk_usage('/').percent

# Função para inserir os dados no MySQL
def insert_data_into_mysql(cpu_percentage, memory_percentage, disk_percentage):
    try:
        connection = mysql.connector.connect(
            host='localhost',
            user='root',
            password='ScottPippen33',
            database='sixtracker'
        )

        cursor = connection.cursor()

        insert_query = "INSERT INTO Registro (valorRegistro, dataRegistro, fkComponente) VALUES (%s, %s, %s)"

        # Inserir percentual de uso da CPU
        data_cpu = (cpu_percentage, datetime.now(), 1)  # ID do componente da CPU é 1
        cursor.execute(insert_query, data_cpu)

        # Inserir percentual de uso da memória
        data_memory = (memory_percentage, datetime.now(), 5)  # ID do componente de memória é 5
        cursor.execute(insert_query, data_memory)

        # Inserir percentual de uso do disco
        data_disk = (disk_percentage, datetime.now(), 10)  # ID do componente de disco é 10 
        cursor.execute(insert_query, data_disk)

        connection.commit()
        print("Dados inseridos com sucesso no MySQL!")

    except mysql.connector.Error as error:
        print(f"Erro ao inserir os dados no MySQL: {error}")

    finally:
        if 'connection' in locals() and connection.is_connected():
            cursor.close()
            connection.close()
            print("Conexão ao MySQL encerrada.")

if __name__ == "__main__":
    cpu_percentage = get_cpu_usage()
    memory_percentage = get_memory_usage()
    disk_percentage = get_disk_usage()

    # Inserindo os percentuais de uso no MySQL para cada tipo de componente
    insert_data_into_mysql(cpu_percentage, memory_percentage, disk_percentage)
