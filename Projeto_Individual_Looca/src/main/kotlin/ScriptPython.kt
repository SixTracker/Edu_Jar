import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object ScriptPython {
    private lateinit var processoPython: Process
    private lateinit var errorStream: InputStreamReader
    private lateinit var errorBufferedReader: BufferedReader

    fun importarScript(fkCPU:Int, fkRAM:Int, fkDISC:Int) {
        val arquivoPy = "Eduardo.py"
        val codigoPython = """
import psutil
import mysql.connector
from datetime import datetime
import pyodbc

def sql_server_connection(server, database, username, password):
    connection_string = f'DRIVER={{SQL Server}};SERVER={'54.146.1.25'};DATABASE={'sixtracker'};UID={'sa'};PWD={'Sixtracker@'}'
    try:
        connection = pyodbc.connect(connection_string)
        return connection
    except pyodbc.Error as ex:
        print(f"Erro ao conectar ao banco de dados: {ex}")
        sys.exit()
        
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
        data_cpu = (cpu_percentage, datetime.now(), ${fkCPU})  # ID do componente da CPU é 363
        cursor.execute(insert_query, data_cpu)

        # Inserir percentual de uso da memória
        data_memory = (memory_percentage, datetime.now(), ${fkRAM})  # ID do componente de memória é 366
        cursor.execute(insert_query, data_memory)

        # Inserir percentual de uso do disco
        data_disk = (disk_percentage, datetime.now(), ${fkDISC})  # ID do componente de disco é 375
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
        """

        File(arquivoPy).writeText(codigoPython)

        // Use ProcessBuilder para iniciar o processo Python
        println("Iniciando o processo Python...")
        val processBuilder = ProcessBuilder("python.exe", arquivoPy)
        processoPython = processBuilder.start()
        println("Processo Python iniciado.")


        // Inicialize as propriedades relacionadas
        errorStream = InputStreamReader(processoPython.errorStream)
        errorBufferedReader = BufferedReader(errorStream)

        val thread = Thread {
            val combinedInputStream = InputStreamReader(processoPython.inputStream)
            val combinedBufferedReader = BufferedReader(combinedInputStream)

            val errorInputStream = InputStreamReader(processoPython.errorStream)
            val errorBufferedReader = BufferedReader(errorInputStream)

            while (true) {
                // Leia a saída padrão
                val line = combinedBufferedReader.readLine()
                if (line == null) break
                println(line)

                // Leia a saída de erro
                val errorLine = errorBufferedReader.readLine()
                if (errorLine == null) break
                println("Erro: $errorLine")
            }
        }

        thread.start()

    }

}