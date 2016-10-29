package avalone.fractals.mandelbrot;

public class Complex 
{
	private double real;
	private double imaginary;
	
	public Complex(double real, double imaginary)
	{
		this.real = real;
		this.imaginary = imaginary;
	}
	
	public Complex square()
    {
		double newReal = real * real - imaginary*imaginary;
		double newImaginary = 2*real*imaginary;
		
        return new Complex(newReal,newImaginary);
    }
	
	public Complex add(Complex other)
	{
		double newReal = real + other.real;
		double newImaginary = imaginary + other.imaginary;
		
		return new Complex(newReal,newImaginary);
	}
	
	public void addSelf(Complex other)
	{
		real = real + other.real;
		imaginary = imaginary + other.imaginary;
	}
	
	public void addSelf(double otherReal,double otherImaginary)
	{
		real = real + otherReal;
		imaginary = imaginary + otherImaginary;
	}
	
	public double abs()
	{
		return Math.hypot(real, imaginary);
	}
}
