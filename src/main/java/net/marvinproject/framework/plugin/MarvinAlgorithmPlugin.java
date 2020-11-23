package net.marvinproject.framework.plugin;

import net.marvinproject.framework.util.MarvinAttributes;

public interface MarvinAlgorithmPlugin extends MarvinPlugin
{
	public void process(MarvinAttributes out);
}
