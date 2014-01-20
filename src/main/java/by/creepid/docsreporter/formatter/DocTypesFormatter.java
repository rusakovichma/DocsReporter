package by.creepid.docsreporter.formatter;

public interface DocTypesFormatter<F,T> {
	
	public T format(F f);
	
	public Class<T> getToClass();
	
	public Class<F> getFromClass();
	
}
