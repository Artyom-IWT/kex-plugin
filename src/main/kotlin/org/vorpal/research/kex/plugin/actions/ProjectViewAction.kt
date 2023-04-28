package org.vorpal.research.kex.plugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.module.ModuleUtil
import org.vorpal.research.kex.plugin.KexRunner
import org.vorpal.research.kex.plugin.psi.fileClassFqName

class ProjectViewAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val psiFile = e.getData(CommonDataKeys.PSI_FILE) ?: return
        val module = ModuleUtil.findModuleForFile(psiFile) ?: return
        val target = psiFile.fileClassFqName?.asString() ?: return

        KexRunner(project).run(module, target)
    }
}