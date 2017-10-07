//
//  ViewController.swift
//  falldown
//
//  Created by Cong on 5/10/17.
//  Copyright © 2017 Microsoft. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    var table:MSTable?
    //var store : MSCoreDataStore?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.setNavigationBarHidden(true, animated: false)
        // Do any additional setup after loading the view.
        let client = MSClient(applicationURLString: "https://falldown.azurewebsites.net")
        table = client.table(withName: "todoitem")
        
//        let test = "hahaha"
//        // Create a predicate that finds items where complete is false
//        let predicate =  NSPredicate(format: "test == %@",test)
//        // Query the TodoItem table
//        table?.read(with: predicate) { (result, error) in
//            if let err = error {
//                print("ERROR ", err)
//            } else if let items = result?.items {
//                for item in items {
//                    print("Todo Item: ", item["text"])
//                }
//            }
//        }
        
//        let newItem = ["text": "fucong", "test": "hahaha", "complete": false] as [String : Any]
//        table!.insert(newItem) { (result, error) in
//            if let err = error {
//                print("ERROR ", err)
//            } else if let item = result {
//                print("Todo Item: ", item["text"])
//            }
//        }
//
//        table!.read { (result, error) in
//            if let err = error {
//                print("ERROR ", err)
//            } else if let items = result?.items {
//                for item in items {
//                    print("Todo Item: ", item["text"])
//                    print(item["test"])
//                }
//            }
//        }
    
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
