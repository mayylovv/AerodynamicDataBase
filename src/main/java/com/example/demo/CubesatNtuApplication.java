package com.example.demo;

import com.example.demo.service.MainCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

@RequiredArgsConstructor
@SpringBootApplication
public class CubesatNtuApplication {

	private final MainCalculationService mainCalculationService;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(CubesatNtuApplication.class, args);
		Scanner scanner = new Scanner(System.in);

		CubesatNtuApplication app = context.getBean(CubesatNtuApplication.class);  // Получаем бины через контекст

		System.out.println("Введите угол атаки:");
		double alfa = scanner.nextDouble();

		System.out.println("Введите высоту атмосферы из списка: 150, 200, 250, 300, 400, 500, 600, 700, 800");
		int heightKm = scanner.nextInt();

		System.out.println("Введите радиус атмосферного шара:");
		double radius = scanner.nextDouble();

		System.out.println("Введите длину Cubesat:");
		double length = scanner.nextDouble();

		System.out.println("CoeficientX:");
		System.out.println(app.mainCalculationService.calculateCoefficientX(radius));
		System.out.println("Density:");
		System.out.println(app.mainCalculationService.getDensity(heightKm));
		System.out.println("Speed:");
		System.out.println(app.mainCalculationService.calculateSpeed(heightKm));
		System.out.println("Area:");
		System.out.println(app.mainCalculationService.calculateArea(radius));
		System.out.println("ForceX:");
		System.out.println(app.mainCalculationService.calculateForceX(heightKm, radius));
		System.out.println("MomentX:");
		System.out.println(app.mainCalculationService.calculateMomentX(heightKm, radius, alfa, length));

		System.out.println("CoeficientY:");
		System.out.println(app.mainCalculationService.calculateCoefficientY(alfa));
		System.out.println("ForceY:");
		System.out.println(app.mainCalculationService.calculateForceY(heightKm, radius, alfa));
		System.out.println("MomentY:");
		System.out.println(app.mainCalculationService.calculateMomentY(heightKm, radius, alfa, length));

		System.out.println("VelocityHead:");
		System.out.println(app.mainCalculationService.calculateVelocityHead(heightKm));

		System.out.println();
		System.out.println("Значения коэффициента аэродинамической подъемной силы при разных углах атаки:");
		int[] alfas = new int[11];
		for (int i = 0, value = -50; i < alfas.length; i++, value += 10) {
			alfas[i] = value;
		}

		for (int a : alfas) {
			// Вызов метода с параметром из массива
			System.out.println("Угол атаки: " + a + "; Cya = " + app.mainCalculationService.calculateCoefficientY(a));
		}


		System.out.println();
		System.out.println("Значения силы лобового сопративления при разных высотах атмосферы:");
		int[] heights = {150, 200, 250, 300, 400, 500, 600, 700, 800};

		for (int h : heights) {
			System.out.println("Высота = " + h + "; ForceX = " + app.mainCalculationService.calculateForceX(h, radius));
		}

		System.out.println();
		System.out.println("Значения подъемной силы при разных высотах атмосферы:");
		for (int h : heights) {
			System.out.println("Высота = " + h + "; ForceY = " + app.mainCalculationService.calculateForceY(h, radius, alfa));
		}

		System.out.println();
		System.out.println("Значение силы лобового сопративления при разных радиусах шара:");
		double[] rrradius = {0.5, 0.75, 1, 1.25, 1.5};

		for (double r: rrradius) {
			System.out.println("Радиус оболочки = " + r + "; ForceX = " + app.mainCalculationService.calculateForceX(heightKm, r));
		}

		context.close();

	}

}
