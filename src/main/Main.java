package src.main;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Дерамида");
        Deramida deramida = new Deramida();
        char ch;
        int key;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("Нажмите \"1\", чтобы добавить новый элемент");
            System.out.println("Нажмите \"2\", чтобы удалить элемент");
            System.out.println("Нажмите \"3\", чтобы добавить сразу 5 элементов");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Введите значение ключа");
                    key = scanner.nextInt();
                    deramida.add(key);
                    break;
                case 2:
                    System.out.println("Введите значение ключа");
                    key = scanner.nextInt();
                    deramida.remove(key);
                    break;
                case 3:
                    System.out.println("Введите пять значений ключей");
                    key = scanner.nextInt();
                    deramida.add(key);
                    key = scanner.nextInt();
                    deramida.add(key);
                    key = scanner.nextInt();
                    deramida.add(key);
                    key = scanner.nextInt();
                    deramida.add(key);
                    key = scanner.nextInt();
                    deramida.add(key);
                    break;
                default:
                    System.out.println("Вы нажали что-то не то...\n");
                    break;
            }

            System.out.println("Новая дерамида:");
            deramida.print();
            System.out.println("\nПродолжить? y/n \n");
            ch = scanner.next().charAt(0);
        } while (ch == 'y');
}
}
