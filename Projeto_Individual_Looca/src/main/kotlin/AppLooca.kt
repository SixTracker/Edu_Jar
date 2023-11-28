import javax.swing.JOptionPane

fun main() {
    val py = ScriptPython

    val fkCPU = JOptionPane.showInputDialog("").toInt()
    val fkRAM = JOptionPane.showInputDialog("").toInt()
    val fkDISC = JOptionPane.showInputDialog("").toInt()

    py.importarScript(fkCPU, fkRAM, fkDISC)

}
