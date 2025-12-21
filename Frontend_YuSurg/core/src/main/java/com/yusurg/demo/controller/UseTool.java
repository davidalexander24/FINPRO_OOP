package com.yusurg.demo.controller;

import com.yusurg.demo.model.Patient;
import com.yusurg.demo.tools.SurgicalTool;

/**
 * Concrete Command for using surgical tools
 */
public class UseTool implements ToolCommand {
    private SurgicalTool tool;
    private Patient patient;

    public UseTool(SurgicalTool tool, Patient patient) {
        this.tool = tool;
        this.patient = patient;
    }

    @Override
    public SurgicalTool.ToolResult execute() {
        // Step 1: Resolve anesthesia state BEFORE executing tool
        boolean canProceed = patient.resolveTurn(tool.getName());

        // If patient died from anesthesia check, return failure result
        if (!canProceed) {
            return new SurgicalTool.ToolResult(false, "Patient died from the action!", false);
        }

        // Step 2: Execute the tool
        SurgicalTool.ToolResult result = tool.use(patient);

        // Step 3: Apply turn-based effects after tool usage (pass tool name to skip effects for certain tools)
        patient.onToolUsed(tool.getName());

        return result;
    }

    @Override
    public String getToolName() {
        return tool.getName();
    }
}

