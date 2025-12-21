package com.yusurg.demo.controller;

import com.yusurg.demo.tools.SurgicalTool;

/**
 * Command Pattern Interface
 * Purpose: Encapsulate tool actions so UI buttons don't hold logic
 */
public interface ToolCommand {
    SurgicalTool.ToolResult execute();
    String getToolName();
}


