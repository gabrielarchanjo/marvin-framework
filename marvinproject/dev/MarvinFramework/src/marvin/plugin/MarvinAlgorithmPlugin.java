package marvin.plugin;

import marvin.util.MarvinAttributes;

public interface MarvinAlgorithmPlugin extends MarvinPlugin
{
	public void process(MarvinAttributes out);
}
